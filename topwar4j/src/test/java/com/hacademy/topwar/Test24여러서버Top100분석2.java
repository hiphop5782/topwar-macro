package com.hacademy.topwar;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacademy.topwar.util.OcrUtils;
import com.hacademy.topwar.vo.ServerUserData;

public class Test24여러서버Top100분석2 {
	public static void main(String[] args) throws Exception {
//		HttpClient client = HttpClient.newHttpClient();
//		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://raw.githubusercontent.com/hiphop5782/topwar-json/refs/heads/main/servers.json")).GET().build();
//		
//		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//		
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode root = mapper.readTree(response.body());
//		
//		JsonNode listNode = root.get("list");
//		if(listNode == null || !listNode.isArray()) return;
//		List<Integer> servers = new ArrayList<>();
//		for(JsonNode node : listNode) {
//			servers.add(node.asInt());
//		}
		final List<Integer> servers = List.of(
				3453
		);
		System.out.println(servers.size()+"개 서버에 대한 분석을 시작합니다");
		
		File targetDir = new File(System.getProperty("user.home") + "/git/topwar-json");
		//스레드 실행 도구
		for(int server : servers) {
			System.out.println("<"+server+"> 서버 분석 시작");
			try {
				File dir = new File(System.getProperty("user.home"), "tw-macro/ocr/"+server);
				List<String> cpList = OcrUtils.doOcrDirectory(dir);
				//List<String> cpList = OcrUtils.doOcrDirectoryByTesseract(dir);
				
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
		}
		
	}
	
}
