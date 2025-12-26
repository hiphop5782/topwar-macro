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
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OcrUtils {
//	private static final String URL = "http://192.168.0.5:5000/ocr";
//	private static final String URL = "http://host.sysout.co.kr:5000/ocr";
	private static final String URL = "http://localhost:5000/ocr";

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
	
	// 정규표현식 패턴: "text":" 뒤에 오는 큰따옴표 안의 문자열
	private static Pattern pattern = Pattern.compile("\"text\":\"(.*?)\"");

	public static List<String> doOcrDirectory(String path) throws IOException {
		List<String> list = new ArrayList<>();
		Path folder = Paths.get(path); // 이미지 폴더 경로
		HttpClient client = HttpClient.newHttpClient();

		Files.list(folder).filter(p -> p.toString().endsWith(".png") || p.toString().endsWith(".tif")).sorted()
			.forEach(imagePath -> {
				try {
					String result = sendImage(client, imagePath);
					System.out.printf("%s [%s]\n\t→%s\n", path, imagePath.getFileName(), result);
					
					Matcher matcher = pattern.matcher(result);
					if(matcher.find()) {
						String cp = matcher.group(1);
						System.out.println("\t→최종CP : "+cp);
						list.add(cp);
					}
					//System.out.println("OCR 완료 (" + imagePath.getFileName() + ")");
				} catch (Exception e) {
					System.err.println("❌ 오류: " + imagePath + " → " + e.getMessage());
				}
			});
		return list;
	}

	public static String doOcrFile(Path imagePath) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
//		String jsonStr = sendImage(client, imagePath);
//		ObjectMapper parser = new ObjectMapper();
//		Map<String, String> map = parser.readValue(jsonStr, new TypeReference<Map<String, String>>() {
//		});
//		System.out.println(imagePath.getFileName() + " → " + map.get("text"));
//		return map.get("text");
		
		try {
			String result = sendImage(client, imagePath);
			Matcher matcher = pattern.matcher(result);
			if(matcher.find()) {
				String value = matcher.group(1);
				System.out.printf("%s → %s%n", imagePath.getFileName(), value);
				return value;
			}
			//System.out.println("OCR 완료 (" + imagePath.getFileName() + ")");
		} catch (Exception e) {
			//System.err.println("❌ 오류: " + imagePath + " → " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> doOcrDirectoryByTesseract(File dir) throws IOException {
		return doOcrDirectoryByTesseract(dir.getAbsolutePath());
	}

	public static List<String> doOcrDirectoryByTesseract(String path) throws IOException {
		Path folder = Paths.get(path); // 이미지 폴더 경로
		System.out.println(folder.toString()+" 분석 시작");

		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
		tesseract.setLanguage("eng");
		tesseract.setVariable("tessedit_char_whitelist", "0123456789.M");
		tesseract.setVariable("tessedit_char_blacklist", "abcdefghijklmnopqrstuvwxyz!@#$%^&*()<>?{}[]|\\/:;'\"`~");
		tesseract.setVariable("load_system_dawg", "F");
		tesseract.setVariable("load_freq_dawg", "F");
		tesseract.setVariable("user_patterns_file", "D:/tesseract-training/patterns.txt");
		tesseract.setOcrEngineMode(1);
		tesseract.setPageSegMode(8);
		tesseract.setVariable("classify_bln_numeric_mode", "1");

		List<String> list = new ArrayList<>();
		
		Files.list(folder).filter(p -> p.toString().endsWith(".tif")).sorted()
				.forEach(imagePath -> {
					try {
						String result = tesseract.doOCR(imagePath.toFile()).trim();
						System.out.println(imagePath+" → "+result);
						list.add(result);
					} catch (TesseractException e) {
						e.printStackTrace();
					}
				});
		return list;
	}
}
