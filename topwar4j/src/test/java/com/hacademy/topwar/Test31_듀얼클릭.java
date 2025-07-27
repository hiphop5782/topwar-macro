package com.hacademy.topwar;

import java.awt.Rectangle;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.hacademy.topwar.ui.ScreenRectDialog;
import com.hacademy.topwar.util.Mouse;

public class Test31_듀얼클릭 {
	public static void main(String[] args) throws Exception {
		GlobalScreen.registerNativeHook();
		
		Rectangle origin = ScreenRectDialog.showDialog();
		Rectangle copy = ScreenRectDialog.showDialog();
		
		GlobalScreen.addNativeMouseListener(new NativeMouseListener() {
			private Object syn = new Object();
			private boolean robotFlag = false;
			@Override
			public void nativeMouseClicked(NativeMouseEvent e) {
				System.out.println("robotFlag = " + robotFlag);
				if(robotFlag) return;
				robotFlag = true;
				
				System.out.println(e.getX()+", "+e.getY()+", "+e.getButton());
				synchronized(syn) {
					Mouse.create().clickL(e.getX() - 1920, e.getY());
					robotFlag = false;
				}
			}
		});
		
	}
}
