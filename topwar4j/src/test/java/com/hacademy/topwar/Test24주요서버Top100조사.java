package com.hacademy.topwar;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.naming.ServiceUnavailableException;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacademy.topwar.ui.ScreenRectDialog;
import com.hacademy.topwar.util.CaptureUtils;
import com.hacademy.topwar.util.GithubUtils;
import com.hacademy.topwar.util.Keyboard;
import com.hacademy.topwar.util.OcrUtils;
import com.hacademy.topwar.vo.ServerUserData;

public class Test24주요서버Top100조사 {
	public static void main(String[] args) throws Exception {
		long begin = System.currentTimeMillis();
		
		//감지영역 설정 및 요청
		boolean usePrevScreen = true;
		
		//감지영역 설정 및 요청
		Rectangle rect;
		if(usePrevScreen) {
			try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(".screen")))){
				rect = (Rectangle)in.readObject();
			}
			catch(Exception e) {
				rect = ScreenRectDialog.showDialog();
				try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(".screen")))){
					out.writeObject(rect);
				}
			}
		}
		else {
			rect = ScreenRectDialog.showDialog();
			try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(".screen")))){
				out.writeObject(rect);
			}
		}
		
		HttpClient client = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://raw.githubusercontent.com/hiphop5782/topwar-json/refs/heads/main/servers.json")).GET().build();
		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.body());
		
		JsonNode listNode = root.get("list");
		if(listNode == null || !listNode.isArray()) return;
		
		List<Integer> servers = new ArrayList<>();
		for(JsonNode node : listNode) {
			servers.add(node.asInt());
		}
		servers.sort((o1, o2)->Math.abs(3453-o1) - Math.abs(3453-o2));
//		final List<Integer> servers = List.of(
//				2566
//		);
		System.out.println(servers.size()+"개 서버에 대한 분석을 시작합니다");
		
		//ESC 설정
		Keyboard.enableEscToQuit();
		
		int count = 0;
		File targetDir = new File(System.getProperty("user.home") + "/git/topwar-json");
		
		for(int server : servers) {
			System.out.println("<"+server+" 캡쳐 시작> ("+(++count) + " / " + servers.size() +")");
			CaptureUtils.top100(rect, server);
			System.out.println("** 캡쳐 완료 **");
//
			File dir = new File(System.getProperty("user.home"), "tw-macro/ocr/"+server);
			
//			System.out.println("<"+server+" box 생성 시작>");
//			TesseractUtils.createBoxInDirectory(dir);
//			System.out.println("** box 생성 완료 **");
			
			try {
				List<String> cpList = OcrUtils.doOcrDirectory(dir);
//				List<String> cpList = OcrUtils.doOcrDirectoryByTesseract(dir);
				
				ServerUserData serverUserData = new ServerUserData(server, cpList);
				serverUserData.saveToJson(targetDir);
				serverUserData.print();
				serverUserData.printAll();
				serverUserData.printCorrect();
				serverUserData.printError();
				System.out.println("** "+server+" 분석 종료 **");
				
				//check ocr state
				if(serverUserData.getOkList().size() < 10)
					throw new Exception("정상 결과물이 10개 미만");
				if(serverUserData.getNokList().size() >= 10)
					throw new Exception("이상 결과물이 10개 이상");
				int cnt = 0;
				for(String cp : serverUserData.getCpList()) {
					if(cp.strip().isEmpty()) {
						cnt++;
						if(cnt >= 10)//10개 이상이면 이상데이터로 간주
							throw new Exception("OCR 오류 10개 이상");
					}
				}
				
				//Github commit and push
				try {
					GithubUtils.commitAndPush();
				} 
				catch (ServiceUnavailableException | IOException | GitAPIException e) {
					throw e;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		long end = System.currentTimeMillis();
		long ms = end - begin;
		Duration duration = Duration.ofMillis(ms); 
		
		System.out.println("프로그램을 종료합니다.");
		System.out.println("소요시간 : " + duration.toHours()+"시간 "+duration.toMinutesPart()+"분");
	}
	
}
