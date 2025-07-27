package com.hacademy.topwar.util;

import java.awt.Window;

import com.hacademy.topwar.ui.LogDialog;

public class LogUtils {
	
	private static LogDialog logDialog = new LogDialog(null);
	
	public static void println(Object obj) {
		if(logDialog != null) {
			logDialog.println(obj);
		}
		System.out.println(obj.toString());
	}

	public static void showDialog(Window window) {
		logDialog.showDialog(window);
	}
}
