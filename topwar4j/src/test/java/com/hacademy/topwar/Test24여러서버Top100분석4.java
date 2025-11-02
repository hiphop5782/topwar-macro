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

public class Test24여러서버Top100분석4 {
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
		servers.sort((o1, o2)->Math.abs(3384-o1) - Math.abs(3384-o2));
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
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("** "+server+" 분석 종료 **");
			try {
				GithubUtils.commitAndPush();
			} catch (ServiceUnavailableException | IOException | GitAPIException e) {
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
