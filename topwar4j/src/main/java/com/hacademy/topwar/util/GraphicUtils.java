package com.hacademy.topwar.util;

import java.awt.Window;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import com.hacademy.topwar.exception.TargetNotFoundException;

public class GraphicUtils {
	private static Map<String, Window> windows = new HashMap<>();
	private static Window currentWindow = null;
	public static void addWindow(String windowTitle, Window window) {
		windows.put(windowTitle, window);
	}
	public static Window getWindow(String windowTitle) {
		return windows.get(windowTitle);
	}
	public static void showWindow(String windowTitle) {
		Window targetWindow = windows.get(windowTitle);
		if(targetWindow == null) 
			throw new TargetNotFoundException();
		
		if(currentWindow != null) 
			currentWindow.setVisible(false);
		
		targetWindow.setVisible(true);
	}
	public static void runWindow(String windowTitle, Window window) {
		addWindow(windowTitle, window);
		showWindow(windowTitle);
	}
	
}

