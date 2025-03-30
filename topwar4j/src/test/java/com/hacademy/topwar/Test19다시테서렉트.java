package com.hacademy.topwar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Data
class Counter {
	int rank;
	int server;
	int stage;
	boolean clear;
	String damage;
}

public class Test19다시테서렉트 {
	public static void main(String[] args) {
		Tesseract tesseract = new Tesseract();
		
		List<Counter> rankList = new ArrayList<>();
		List<File> errorList = new ArrayList<>();

        try {
            // 1. tessdata 경로 설정 (traineddata 파일이 있는 폴더)
        	tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");

            // 2. 사용할 언어 설정 (eng = 영어, kor = 한글)
            tesseract.setLanguage("eng"); // 또는 "kor", "eng+kor"

            for(int i=1; i <= 500; i++) {
            	// 3. OCR 수행할 이미지
                File server = new File("./ocr/kartz/kartz-"+i+"-server.png");
                File stage = new File("./ocr/kartz/kartz-"+i+"-stage.png");

                // 4. OCR 실행
                String serverResult = tesseract.doOCR(server).trim();
                Counter counter = new Counter();
                counter.rank = i;
                try {
                	counter.server = Integer.parseInt(serverResult.trim());
                }
                catch(Exception e) {
                	errorList.add(server);
                }
                
                String stageResult = tesseract.doOCR(stage).trim();
                try {
                	counter.stage = Integer.parseInt(stageResult);
                }
                catch(Exception e) {
                	errorList.add(stage);
                }
                
                File damage = new File("./ocr/kartz/kartz-"+i+"-damage.png");
                if(damage.exists()) {
                	counter.damage = tesseract.doOCR(damage).trim();
                }
                
                rankList.add(counter);
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        
        for(Counter c : rankList) {
        	System.out.println(c);
        }
	}
}		
