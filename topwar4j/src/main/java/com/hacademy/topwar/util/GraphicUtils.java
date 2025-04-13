package com.hacademy.topwar.util;

import java.awt.Window;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import com.hacademy.topwar.exception.TargetNotFoundException;

public class GraphicUtils {
	private static Map<Class<?>, Window> windows = new HashMap<>();
	private static Window currentWindow = null;
	public static void addWindow(Class<?> clazz, Window window) {
		windows.put(clazz, window);
	}
	public static Window getWindow(Class<?> clazz) {
		return windows.get(clazz);
	}
	public static void showWindow(Class<?> clazz) {
		Window targetWindow = windows.get(clazz);
		if(targetWindow == null) 
			throw new TargetNotFoundException();
		
		if(currentWindow != null) 
			currentWindow.setVisible(false);
		
		targetWindow.setVisible(true);
	}
	public static void runWindow(Class<?> clazz, Window window) {
		addWindow(clazz, window);
		showWindow(clazz);
	}
	
}

