package com.hacademy.topwar;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.hacademy.topwar.ui.ScreenRectDialog;
import com.hacademy.topwar.util.CaptureUtils;
import com.hacademy.topwar.util.OcrUtils;
import com.hacademy.topwar.vo.ServerUserData;

public class Test23서버Top100분석 {
	public static void main(String[] args) throws Exception {
		
		//서버 설정
		int server = 3088;
		boolean usePrevScreen = true;
		
		File dir = new File(System.getProperty("user.home"), "tw-macro/ocr/"+server);
		
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
		}
		
		CaptureUtils.top100(rect, server);
		
		List<String> cpList = OcrUtils.doOcrDirectory(dir);
		//List<String> cpList = OcrUtils.doOcrDirectoryByTesseract(dir);
		
		ServerUserData serverUserData = new ServerUserData(server, cpList);
		serverUserData.saveToJson(new File("C:\\Users\\user1\\git\\topwar-json"));
		serverUserData.print();
		serverUserData.printAll();
		serverUserData.printCorrect();
		serverUserData.printError();
	}
}
