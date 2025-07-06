package com.hacademy.topwar.util;

import java.io.File;
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
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OcrUtils {
//	private static final String URL = "http://192.168.30.17:5000/ocr";
	private static final String URL = "http://host.sysout.co.kr:5000/ocr";

	public static String sendImage(HttpClient client, Path imagePath) throws IOException, InterruptedException {
		String boundary = "----WebKitFormBoundary" + UUID.randomUUID().toString().replace("-", "");

		String fileName = imagePath.getFileName().toString();
		byte[] fileContent = Files.readAllBytes(imagePath);

		StringBuilder sb = new StringBuilder();
		sb.append("--").append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"image\"; filename=\"").append(fileName).append("\"\r\n");
		sb.append("Content-Type: image/png\r\n");
		sb.append("\r\n");

		byte[] bodyStart = sb.toString().getBytes();
		byte[] bodyEnd = ("\r\n--" + boundary + "--\r\n").getBytes();

		byte[] fullBody = new byte[bodyStart.length + fileContent.length + bodyEnd.length];
		System.arraycopy(bodyStart, 0, fullBody, 0, bodyStart.length);
		System.arraycopy(fileContent, 0, fullBody, bodyStart.length, fileContent.length);
		System.arraycopy(bodyEnd, 0, fullBody, bodyStart.length + fileContent.length, bodyEnd.length);

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL))
				.header("Content-Type", "multipart/form-data; boundary=" + boundary)
				.POST(HttpRequest.BodyPublishers.ofByteArray(fullBody)).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return response.body().trim();
	}

	public static List<String> doOcrDirectory(File dir) throws IOException {
		return doOcrDirectory(dir.getAbsolutePath());
	}

	public static List<String> doOcrDirectory(String path) throws IOException {
		List<String> list = new ArrayList<>();
		Path folder = Paths.get(path); // 이미지 폴더 경로
		HttpClient client = HttpClient.newHttpClient();

		Files.list(folder).filter(p -> p.toString().endsWith(".png") || p.toString().endsWith(".jpg")).sorted()
				.forEach(imagePath -> {
					try {
						String result = sendImage(client, imagePath);
						System.out.printf("%s [%s] → %s%n", path, imagePath.getFileName(), result);
						list.add(result);
						//System.out.println("OCR 완료 (" + imagePath.getFileName() + ")");
					} catch (Exception e) {
						System.err.println("❌ 오류: " + imagePath + " → " + e.getMessage());
					}
				});

		ObjectMapper parser = new ObjectMapper();
		return list.stream().map(json -> {
			try {
				Map<String, String> map = parser.readValue(json, new TypeReference<Map<String, String>>() {
				});
				return map.get("text");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}).filter(Objects::nonNull).toList();
	}

	public static String doOcrFile(Path imagePath) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		String jsonStr = sendImage(client, imagePath);
		ObjectMapper parser = new ObjectMapper();
		Map<String, String> map = parser.readValue(jsonStr, new TypeReference<Map<String, String>>() {
		});
		System.out.println(imagePath.getFileName() + " → " + map.get("text"));
		return map.get("text");
	}

	public static List<String> doOcrDirectoryByTesseract(File dir) throws IOException {
		return doOcrDirectoryByTesseract(dir.getAbsolutePath());
	}

	public static List<String> doOcrDirectoryByTesseract(String path) throws IOException {
		Path folder = Paths.get(path); // 이미지 폴더 경로

		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
		tesseract.setLanguage("eng");
		tesseract.setVariable("tessedit_char_whitelist", "0123456789.M");

		List<String> list = new ArrayList<>();
		Files.list(folder).filter(p -> p.toString().endsWith(".png") || p.toString().endsWith(".jpg")).sorted()
				.forEach(imagePath -> {
					try {
						String result = tesseract.doOCR(imagePath.toFile()).trim();
						list.add(result);
					} catch (TesseractException e) {
						e.printStackTrace();
					}
				});
		return list;
	}
}
