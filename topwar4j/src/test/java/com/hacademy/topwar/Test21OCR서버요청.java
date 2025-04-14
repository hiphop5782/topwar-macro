package com.hacademy.topwar;

import java.io.IOException;
import java.util.List;

import com.hacademy.topwar.util.OcrUtils;

public class Test21OCR서버요청 {
	public static void main(String[] args) throws IOException, InterruptedException {
		List<String> list = OcrUtils.doOcrDirectory("C:\\Users\\user1\\git\\topwar-macro\\topwar4j\\ocr\\debug\\3396");
		for(String str : list) {
			System.out.println(str);
		}
	}
}
