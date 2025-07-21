package com.hacademy.topwar;

import java.awt.Rectangle;
import java.util.List;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.hacademy.topwar.util.WinUserEx;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.WPARAM;


public class Test31클릭미러링 {
	public static void main(String[] args) throws NativeHookException {
		List<Rectangle> rectList = List.of(
			new Rectangle(0, 0, 200, 200),
			new Rectangle(200, 0, 200, 200),
			new Rectangle(400, 0, 200, 200)
		);
		
		NativeMouseInputListener listener = new NativeMouseInputListener() {
			@Override
			public void nativeMouseClicked(NativeMouseEvent e) {
				
				//창 열거 및 필터링
				User32.INSTANCE.EnumWindows((hwnd, data)->{
					char[] buffer = new char[512];
					User32.INSTANCE.GetWindowText(hwnd, buffer, buffer.length);
					String title = Native.toString(buffer);
					if(title.contains("Topwar")) {
						System.out.println("title = " + title);
						
						RECT rect = new RECT();
						User32.INSTANCE.GetWindowRect(hwnd, rect);
						
						int x = e.getX() % 200;
						int y = e.getY() % 200;
						int lParam = (y << 16) | (x & 0xFFFF);

		                // 3. 클릭 메시지 보내기
		                System.out.println("Clicking at: " + x + ", " + y);
		                User32.INSTANCE.SendMessage(hwnd, WinUserEx.WM_LBUTTONDOWN, new WPARAM(0), new LPARAM(lParam));
		                try {
							Thread.sleep(50);
						} catch (InterruptedException e1) {}
		                User32.INSTANCE.SendMessage(hwnd, WinUserEx.WM_LBUTTONUP, new WPARAM(0), new LPARAM(lParam));
					}
					return true;
				}, null);
				
			}
			@Override
			public void nativeMouseDragged(NativeMouseEvent e) {
//				System.out.println("native mouse dragged");
			}
		};
		
		GlobalScreen.registerNativeHook();
		GlobalScreen.addNativeMouseListener(listener);
		GlobalScreen.addNativeMouseMotionListener(listener);
	}
}
