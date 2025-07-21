package com.hacademy.topwar;

import java.awt.Rectangle;
import java.util.List;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.hacademy.topwar.macro.MacroStatus;
import com.hacademy.topwar.util.PropertyManager;
import com.hacademy.topwar.util.RectData;
import com.hacademy.topwar.util.User32Ex;
import com.hacademy.topwar.util.WinUserEx;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.WPARAM;


public class Test31클릭미러링 {
	public static void main(String[] args) throws NativeHookException {
		MacroStatus status = PropertyManager.getMacroStatus();
		List<RectData> screenList = status.getScreenList();
		
		NativeMouseInputListener listener = new NativeMouseInputListener() {
			@Override
			public void nativeMouseClicked(NativeMouseEvent e) {
				RectData target = null;
				for(RectData data : screenList) {
					Rectangle rect = data.toRectangle();
					if(rect.contains(e.getPoint())) {
						target = data;
						break;
					}
				}
				if(target == null) return;
				
				System.out.println(target);
				
				//상대좌표 환산
				int offsetX = e.getPoint().x - target.x;
				int offsetY = e.getPoint().y - target.y;
				System.out.println("클릭한 절대좌표 : (" + e.getPoint()+")");
				System.out.println("클릭한 상대좌표 : ("+offsetX+", "+offsetY+")");
				
				//클릭 처리
				for(RectData data : screenList) {
					if(data == target) continue;
					
					Rectangle rect = data.toRectangle();
					int targetX = rect.x + offsetX;
					int targetY = rect.y + offsetY;
					System.out.println("target ("+targetX+", "+targetY+")");
					
					POINT pt = new POINT();
					pt.x = targetX;
					pt.y = targetY;
					
					HWND hwnd = User32Ex.INSTANCE.WindowFromPoint(pt);
					System.out.println("hwnd = " + hwnd);
					if(hwnd == null) continue;
					
					// 클릭 메시지 전송
		            int lParam = (targetY << 16) | (targetX & 0xFFFF);
		            User32.INSTANCE.SendMessage(hwnd, WinUserEx.WM_LBUTTONDOWN, new WPARAM(0), new LPARAM(lParam));
		            try { Thread.sleep(50); } catch(Exception ex) {}
		            User32.INSTANCE.SendMessage(hwnd, WinUserEx.WM_LBUTTONUP, new WPARAM(0), new LPARAM(lParam));

		            System.out.println("Clicked at: " + targetX + ", " + targetY);
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
