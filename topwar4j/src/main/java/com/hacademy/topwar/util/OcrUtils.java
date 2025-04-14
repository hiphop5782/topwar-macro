package com.hacademy.topwar.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OcrUtils {
	private static final String URL = "http://192.168.30.17:5000/ocr";
	private static final String BOUNDARY = "----OCRJavaBoundary";

	public static String sendImage(HttpClient client, Path imagePath) throws IOException, InterruptedException {
		byte[] fileContent = Files.readAllBytes(imagePath);
		String fileName = imagePath.getFileName().toString();

		String bodyHeader = "--" + BOUNDARY + "\r\n" + "Content-Disposition: form-data; name=\"image\"; filename=\""
				+ fileName + "\"\r\n" + "Content-Type: image/png\r\n\r\n";

		String bodyFooter = "\r\n--" + BOUNDARY + "--\r\n";

		byte[] bodyStart = bodyHeader.getBytes();
		byte[] bodyEnd = bodyFooter.getBytes();

		byte[] fullBody = new byte[bodyStart.length + fileContent.length + bodyEnd.length];
		System.arraycopy(bodyStart, 0, fullBody, 0, bodyStart.length);
		System.arraycopy(fileContent, 0, fullBody, bodyStart.length, fileContent.length);
		System.arraycopy(bodyEnd, 0, fullBody, bodyStart.length + fileContent.length, bodyEnd.length);

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL))
				.header("Content-Type", "multipart/form-data; boundary=" + BOUNDARY)
				.POST(HttpRequest.BodyPublishers.ofByteArray(fullBody)).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		return response.body().trim();
	}

	public static List<String> doOcrDirectory(String path) throws IOException {
		List<String> list = new ArrayList<>();
		Path folder = Paths.get(path); // 이미지 폴더 경로
		HttpClient client = HttpClient.newHttpClient();

		Files.list(folder)
		.filter(p -> p.toString().endsWith(".png") || p.toString().endsWith(".jpg")).sorted()
		.forEach(imagePath -> {
			try {
				String result = sendImage(client, imagePath);
				//System.out.printf("[%s] → %s%n", imagePath.getFileName(), result);
				list.add(result);
			} catch (Exception e) {
				System.err.println("❌ 오류: " + imagePath + " → " + e.getMessage());
			}
		});
		return list;
	}
}
