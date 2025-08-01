package com.hacademy.topwar;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.hacademy.topwar.macro.MacroStatus;
import com.hacademy.topwar.util.PropertyManager;
import com.hacademy.topwar.util.RectData;
import com.hacademy.topwar.util.User32Ex;
import com.hacademy.topwar.util.WinUserEx;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.WPARAM;


public class Test31클릭미러링 {
	public static void main(String[] args) throws NativeHookException {
		MacroStatus status = PropertyManager.getMacroStatus();
		List<RectData> screenList = status.getScreenList();
		
		NativeMouseInputListener listener = new NativeMouseInputListener() {
			@Override
			public void nativeMouseClicked(NativeMouseEvent e) {
				
				int clickX = e.getX();
				int clickY = e.getY();
				
				//클릭한 창 찾기
				RectData trigger = null;
				for(RectData rectData : screenList) {
					if(rectData.active == false) continue;
					Rectangle inside = rectData.toRectangle();
					if(inside.contains(e.getPoint())) {
						trigger = rectData;
						break;
					}
				}
				if(trigger == null) return;
				
				//내부 창 상대좌표 계산
				int relativeX = clickX - trigger.x;
				int relativeY = clickY - trigger.y;
				System.out.println("클릭한 상대위치 : ("+relativeX+", "+relativeY+")");
				
				//창 열거 및 필터링
				User32.INSTANCE.EnumWindows((hwnd, data)->{
					char[] buffer = new char[512];
					User32.INSTANCE.GetWindowText(hwnd, buffer, buffer.length);
					String title = Native.toString(buffer);
					if(title.startsWith("Top War | Official Web Game")) {
						RECT rect = new RECT();
						User32.INSTANCE.GetWindowRect(hwnd, rect);
						System.out.println("창 찾음 : "+rect + ", title = " + title);
						
						Rectangle outside = rect.toRectangle();
						for(RectData rectData : screenList) {
							if(rectData.active == false) continue;
							Rectangle inside = rectData.toRectangle();
							Rectangle fixedInside = new Rectangle(inside.x + 50, inside.y + 50, inside.width - 100, inside.height-100);
							if(outside.contains(fixedInside)) {//완전 포함
//							if(outside.intersects(inside)) {//1px이라도 포함
								rectData.outside = outside;
								rectData.hwnd = hwnd;
								rectData.title = title;
							}
						}
					}
					return true;
				}, null);
				
				// 3. 클릭 메시지 보내기
				for(RectData rectData : screenList) {
					if(rectData.active == false) continue;
					if(rectData.hwnd == null) continue;
					if(rectData == trigger) continue;

					Rectangle inside = rectData.toRectangle();
					POINT pt = new POINT();
					pt.x = inside.x + relativeX;
					pt.y = inside.y + relativeY;
					int beforeX = pt.x;
					int beforeY = pt.y;
					
					// 클릭 메시지 전송
					try {
						// 좌표 변환
						User32Ex.INSTANCE.ScreenToClient(rectData.hwnd, pt);
						int lParam = (pt.y << 16) | (pt.x & 0xFFFF);

						// 포커스 주기
						//User32.INSTANCE.SetForegroundWindow(rectData.hwnd);
						//Thread.sleep(100);
						
						System.out.println("hwnd = " + rectData.hwnd);
						System.out.println("Before ("+beforeX+","+beforeY+") → After("+pt.x+","+pt.y+")");

						// PostMessage로 클릭 시도
						User32.INSTANCE.PostMessage(rectData.hwnd, WinUserEx.WM_LBUTTONDOWN, new WPARAM(0), new LPARAM(lParam));
						Thread.sleep(50);
						User32.INSTANCE.PostMessage(rectData.hwnd, WinUserEx.WM_LBUTTONUP, new WPARAM(0), new LPARAM(lParam));
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}

				}
				
				
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
