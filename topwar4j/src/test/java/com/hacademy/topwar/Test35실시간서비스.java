package com.hacademy.topwar;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacademy.topwar.ui.ScreenRectDialog;
import com.hacademy.topwar.util.js.JavascriptUtil;
import com.hacademy.topwar.util.js.ServerInfo;

public class Test35실시간서비스 {
	public static void main(String[] args) {
		int port = 55555;

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("======================================");
			System.out.println("매크로 서버가 시작되었습니다.");
			System.out.println("포트 번호: " + port);
			System.out.println("======================================");

			while (true) {
				// Socket 수락 후 try-with-resources로 리소스 자동 닫기
				try (Socket clientSocket = serverSocket.accept();
						BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
						PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true)) {

					// 1. 요청의 첫 번째 줄 읽기 (Method, Path)
					String requestLine = in.readLine();
					if (requestLine == null || requestLine.trim().isEmpty()) {
						continue; // 데이터가 없으면 다음 요청 대기
					}

					System.out.println("로그 - 요청 원문: " + requestLine);

					String[] requestParts = requestLine.split(" ");
					if (requestParts.length < 2) continue;
					
					String method = requestParts[0]; // GET, POST, OPTIONS
					String path = requestParts[1];   // /?server=3223

					// 2. CORS 해결을 위한 OPTIONS (Preflight) 요청 처리
					if ("OPTIONS".equalsIgnoreCase(method)) {
						out.println("HTTP/1.1 204 No Content");
						out.println("Access-Control-Allow-Origin: *");
						out.println("Access-Control-Allow-Methods: GET, POST, OPTIONS");
						out.println("Access-Control-Allow-Headers: Content-Type, Authorization");
						out.println("Access-Control-Max-Age: 86400");
						out.println();
						out.flush();
						continue; // 중요: return 대신 continue를 써야 서버가 꺼지지 않음
					}

					// 3. 서버 번호 추출 (URL 파라미터 체크)
					int serverNumber = 0;
					if (path.contains("server=")) {
						String query = path.substring(path.indexOf("?") + 1);
						for (String param : query.split("&")) {
							if (param.startsWith("server=")) {
								try {
									serverNumber = Integer.parseInt(param.split("=")[1].replaceAll("[^0-9]", ""));
								} catch (Exception e) {}
							}
						}
					}

					// 4. 나머지 헤더 읽기 및 Content-Length 확인
					String headerLine;
					int contentLength = 0;
					while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
						if (headerLine.startsWith("Content-Length:")) {
							contentLength = Integer.parseInt(headerLine.substring(15).trim());
						}
					}

					// 5. POST 본문에서 서버 번호 추출 (URL에 없었을 경우)
					if (contentLength > 0 && serverNumber == 0) {
					    StringBuilder sb = new StringBuilder();
					    int readByte;
					    int count = 0;
					    
					    // contentLength만큼 정확하게 바이트 단위로 읽기
					    while (count < contentLength && (readByte = in.read()) != -1) {
					        sb.append((char) readByte);
					        count++;
					    }
					    
					    String body = sb.toString();
					    System.out.println("로그 - 수신된 Body: " + body); // 본문 내용 확인용 로그
					    
					    // 숫자만 추출 (예: {"server":3223} 이나 server=3223 모두에서 3223만 추출)
					    String numericOnly = body.replaceAll("[^0-9]", "");
					    if (!numericOnly.isEmpty()) {
					        serverNumber = Integer.parseInt(numericOnly);
					    }
					}

					// 6. 매크로 실행 및 최종 응답
					if (serverNumber > 0) {
						try {
							String macroResult = executeYourMacro(serverNumber);
							out.println("HTTP/1.1 200 OK");
							out.println("Content-Type: application/json; charset=UTF-8");
							out.println("Access-Control-Allow-Origin: *");
							out.println();
							out.println(macroResult);
							out.flush();
						} catch (Exception e) {
							sendErrorResponse(out, 500, "Macro Logic Error: " + e.getMessage());
						}
					} else {
						sendErrorResponse(out, 400, "Server number not found in request.");
					}

				} catch (Exception e) {
					System.err.println("요청 처리 중 에러: " + e.getMessage());
				}
			} // while end
		} catch (IOException e) {
			System.err.println("서버 소켓 에러: " + e.getMessage());
		}
	}

	private static String executeYourMacro(int serverNumber) throws InterruptedException, IOException {
		System.out.println(">>> " + serverNumber + " 서버 정보 조회 시작...");

		Rectangle rect = setRect(true);
		List<ServerInfo> servers = JavascriptUtil.getAllServers2(rect, false);
		
		ServerInfo serverInfo = JavascriptUtil.getServerInfo2(rect, serverNumber);
		
		ServerInfo searchResult = servers.stream().filter(server->server.getServerNumber() == serverNumber).findFirst().orElseThrow();
		searchResult.setPlayerList(serverInfo.getPlayerList());
		searchResult.setAllianceList(serverInfo.getAllianceList());
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(searchResult);
	}

	public static Rectangle setRect(boolean usePrevScreen) throws IOException {
		Rectangle rect;
		if (usePrevScreen) {
			try (ObjectInputStream in = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(".screen")))) {
				rect = (Rectangle) in.readObject();
			} catch (Exception e) {
				rect = ScreenRectDialog.showDialog();
				saveRect(rect);
			}
		} else {
			rect = ScreenRectDialog.showDialog();
			saveRect(rect);
		}
		return rect;
	}

	private static void saveRect(Rectangle rect) {
		try (ObjectOutputStream out = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(".screen")))) {
			out.writeObject(rect);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void sendErrorResponse(PrintWriter out, int statusCode, String message) {
		out.println("HTTP/1.1 " + statusCode + " Error");
		out.println("Content-Type: text/plain; charset=UTF-8");
		out.println("Access-Control-Allow-Origin: *");
		out.println();
		out.println(message);
		out.flush();
		System.out.println("에러 응답 (" + statusCode + "): " + message);
	}
}