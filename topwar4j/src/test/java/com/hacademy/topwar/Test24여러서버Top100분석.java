package com.hacademy.topwar;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacademy.topwar.ui.ScreenRectDialog;
import com.hacademy.topwar.util.CaptureUtils;
import com.hacademy.topwar.util.OcrUtils;
import com.hacademy.topwar.vo.ServerUserData;

public class Test24여러서버Top100분석 {
	private static int count=0;
	public static void main(String[] args) throws Exception {
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
		
		InputStream in = Topwar4jApplication.class
				.getClassLoader()
				.getResourceAsStream("servers.json");
		if(in == null) return;
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(in);
		
		JsonNode listNode = root.get("list");
		if(listNode == null || !listNode.isArray()) return;
		
		List<Integer> servers = new ArrayList<>();
		for(JsonNode node : listNode) {
			servers.add(node.asInt());
		}
//		final List<Integer> servers = List.of(
//		 2157
//		);
		//Collections.shuffle(servers);
		System.out.println(servers.size()+"개 서버에 대한 분석을 시작합니다");
		
		//스레드 실행 도구
		ExecutorService executor = Executors.newFixedThreadPool(4);
		for(int server : servers) {
			System.out.println("<"+server+" 분석 시작> ("+(++count) + " / " + servers.size() +")");
			CaptureUtils.top100(rect, server);
			
			executor.submit(()->{
				try {
					File dir = new File(System.getProperty("user.home"), "tw-macro/ocr/"+server);
					List<String> cpList = OcrUtils.doOcrDirectory(dir);
					//List<String> cpList = OcrUtils.doOcrDirectoryByTesseract(dir);
					
					ServerUserData serverUserData = new ServerUserData(server, cpList);
					serverUserData.saveToJson(new File("C:\\Users\\hwang\\git\\topwar-json"));
					serverUserData.print();
					serverUserData.printAll();
					serverUserData.printCorrect();
					serverUserData.printError();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				System.out.println("** "+server+" 분석 종료 **");
				count++;
				if(count == servers.size()) {
					System.exit(0);
				}
			});
		}
	}
}
