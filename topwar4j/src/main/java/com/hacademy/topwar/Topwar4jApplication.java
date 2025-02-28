package com.hacademy.topwar;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.hacademy.topwar.ui.MainFrame;
import com.hacademy.topwar.util.GraphicUtils;

public class Topwar4jApplication {
	public static void main(String[] args) {
		FlatMacLightLaf.setup();
		
		try {
			File keyFile = new File(System.getProperty("user.dir"), ".certkey");
			String key = "empty";
			try(Scanner sc = new Scanner(keyFile);) {
				key = sc.nextLine();
			}
			catch(Exception e) {}
			
			HttpClient client = HttpClient.newHttpClient();
			
			HttpRequest request = HttpRequest.newBuilder()
					.uri(new URI("https://res.sysout.co.kr/tw-macro/response?key="+key))
					.GET().build();
			
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if(response.statusCode() != 200) {
				throw new Exception("인증 실패");
			}
			
			String jsonStr = response.body();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(jsonStr);
			
			String result = root.get("result").asText();
			String message = root.get("message").asText();
			if(result == null || !result.equals("success")) {
				throw new Exception(message);
			}
			
			JOptionPane.showMessageDialog(null, message, "TW-Macro", JOptionPane.INFORMATION_MESSAGE);
			
			MainFrame frame = new MainFrame();
			GraphicUtils.runWindow("main", frame);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "실행 오류 발생\n개발자에게 문의하세요\n"+e.getMessage(), "오류 발생", JOptionPane.WARNING_MESSAGE);
		}
	}
}
