package com.hacademy.topwar;

import java.awt.Rectangle;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacademy.topwar.ui.ScreenRectDialog;
import com.hacademy.topwar.util.CaptureUtils;
import com.hacademy.topwar.util.OcrUtils;
import com.hacademy.topwar.util.ServerUserData;

public class Test23서버Top100분석 {
	public static void main(String[] args) throws Exception {
		
		//서버 설정
		int server = 3384;
		
		File dir = new File(System.getProperty("user.home"), "tw-macro/ocr/"+server);
		
		//감지영역 설정 및 요청
		Rectangle rect = ScreenRectDialog.showDialog();
		
		//CaptureUtils.top100(rect, server);
		
		List<String> cpList = OcrUtils.doOcrDirectory(dir);
		//List<String> cpList = OcrUtils.doOcrDirectoryByTesseract(dir);
		
		ObjectMapper mapper = new ObjectMapper();
		File target = new File("ocr/"+server+".json");
		mapper.writeValue(target, cpList);
		
		ServerUserData serverUserData = new ServerUserData(server, cpList);
		serverUserData.print();
		serverUserData.printAll();
		serverUserData.printCorrect();
		serverUserData.printError();
	}
}
