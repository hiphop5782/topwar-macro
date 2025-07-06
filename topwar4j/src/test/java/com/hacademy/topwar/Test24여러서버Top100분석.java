package com.hacademy.topwar;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacademy.topwar.ui.ScreenRectDialog;
import com.hacademy.topwar.util.CaptureUtils;
import com.hacademy.topwar.util.Keyboard;

public class Test24여러서버Top100분석 {
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
		
//		HttpClient client = HttpClient.newBuilder().build();
//		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://raw.githubusercontent.com/hiphop5782/topwar-json/refs/heads/main/servers.json")).GET().build();
//		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
//		
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode root = mapper.readTree(response.body());
//		
//		JsonNode listNode = root.get("list");
//		if(listNode == null || !listNode.isArray()) return;
//		
//		List<Integer> servers = new ArrayList<>();
//		for(JsonNode node : listNode) {
//			servers.add(node.asInt());
//		}
		final List<Integer> servers = List.of(
				3223
		);
		System.out.println(servers.size()+"개 서버에 대한 분석을 시작합니다");
		
		//ESC 설정
		Keyboard.enableEscToQuit();
		
		//스레드 실행 도구
		int count = 0;
		for(int server : servers) {
			System.out.println("<"+server+" 캡쳐 시작> ("+(++count) + " / " + servers.size() +")");
			CaptureUtils.top100(rect, server);
			System.out.println("** 캡쳐 완료 **");
		}
		
		System.exit(0);
		
	}
	
}
