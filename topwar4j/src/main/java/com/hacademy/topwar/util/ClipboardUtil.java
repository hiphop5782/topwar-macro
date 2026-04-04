package com.hacademy.topwar.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class ClipboardUtil {
	public static void copyToClipboard(String text) {
		StringSelection selection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, null);
	}
	public static String getFromClipboard() {
        try {
            // 1. 시스템 클립보드 호출
            Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            
            // 2. 클립보드에 텍스트 데이터가 있는지 확인
            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                // 3. 문자열로 변환하여 반환
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception e) {
            System.err.println("클립보드 읽기 오류: " + e.getMessage());
        }
        return null;
    }
	public static String waitForClipboardData(String previousData) throws InterruptedException {
		if(previousData != null && previousData.startsWith("javascript:")) {
			previousData = previousData.substring(1);
		}
		
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    int maxAttempts = 20; // 0.1초씩 20번 = 최대 2초
	    
	    for (int i = 0; i < maxAttempts; i++) {
	        try {
	            Transferable contents = clipboard.getContents(null);
	            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	                String currentData = (String) contents.getTransferData(DataFlavor.stringFlavor);
	                
	                // 1. 데이터가 존재하고
	                // 2. 이전 데이터(자바가 JS에 보냈던 값)와 달라졌다면 복사 성공으로 간주
	                if (currentData != null && !currentData.equals(previousData)) {
	                    return currentData;
	                }
	            }
	        } catch (IllegalStateException e) {
	            // 다른 프로세스가 클립보드를 점유 중일 때 발생 (재시도 필요)
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        Thread.sleep(100); // 0.1초 대기
	    }
	    return null; // 2초 초과 시 타임아웃
	}
}
