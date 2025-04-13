package com.hacademy.topwar;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
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
			System.setProperty("https.protocols", "TLSv1.2");
			File dir = new File(Topwar4jApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			File keyFile = new File(dir, ".certkey");
			String key = "empty";
			try(Scanner sc = new Scanner(keyFile);) {
				key = sc.nextLine();
			}
			catch(Exception e) {}
			
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, null, null);
			
	        // 강제적으로 TLS 1.2 및 특정 Cipher Suite 설정
	        SSLParameters sslParameters = new SSLParameters();
	        sslParameters.setProtocols(new String[]{"TLSv1.2"});
	        sslParameters.setCipherSuites(new String[]{
	            "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
	            "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
	            "TLS_RSA_WITH_AES_256_GCM_SHA384",
	            "TLS_RSA_WITH_AES_128_GCM_SHA256"
	        });

			HttpClient client = HttpClient.newBuilder()
					.sslContext(sslContext)
					.sslParameters(sslParameters)
					.build();
			
			HttpRequest request = HttpRequest.newBuilder()
					.uri(new URI("https://res.sysout.co.kr/tw-macro/response?key="+key))
					.GET().build();
			
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if(response.statusCode() != 200) {
				throw new Exception("인증 서버가 응답하지 않습니다");
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
			GraphicUtils.runWindow(MainFrame.class, frame);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "실행 오류 발생\n개발자에게 문의하세요\n"+e.getMessage(), "오류 발생", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}
}
