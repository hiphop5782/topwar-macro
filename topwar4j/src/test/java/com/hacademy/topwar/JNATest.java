package com.hacademy.topwar;

import com.hacademy.topwar.util.JNativeLoader;
import com.hacademy.topwar.util.User32Ex;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

public class JNATest {
    public static void main(String[] args) throws Exception {
    	JNativeLoader.init();
    	
    	WNDENUMPROC debugProc = (hwnd, data) -> {
    	    char[] buffer = new char[1024];
    	    User32Ex.INSTANCE.GetWindowTextW(hwnd, buffer, 1024);
    	    String title = Native.toString(buffer).trim();
    	    
    	    // 1. 모든 창을 출력하지 말고, 'War'나 'Top'이 들어간 모든 창을 출력
    	    if (title.toLowerCase().contains("war") || title.toLowerCase().contains("top")) {
    	        System.out.println("임계값 매칭 발견: [" + title + "] / 핸들: " + hwnd);
    	    }
    	    
    	    // 2. 만약 타이틀이 아예 안 나온다면 클래스 이름을 확인
    	    if (title.isEmpty()) {
    	        char[] classBuffer = new char[256];
    	        User32.INSTANCE.GetClassName(hwnd, classBuffer, 256);
    	        String className = Native.toString(classBuffer);
    	        // 특정 앱들은 타이틀 없이 클래스명으로만 존재할 수 있음
    	        if(className.contains("Qt") || className.contains("Chrome")) {
    	             // System.out.println("타이틀 없는 후보 클래스: " + className);
    	        }
    	    }
    	    return true;
    	};

    	// 별도 스레드에서 실행 (데드락 방지)
    	new Thread(() -> {
    	    User32Ex.INSTANCE.EnumWindows(debugProc, null);
    	}).start();
    }
}