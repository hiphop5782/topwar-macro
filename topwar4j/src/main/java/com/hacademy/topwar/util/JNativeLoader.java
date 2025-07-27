package com.hacademy.topwar.util;

import java.io.File;

import com.github.kwhat.jnativehook.GlobalScreen;

public class JNativeLoader {
	public static void init() throws Exception {
		// JNativeHook.dll 수동 로드
		System.setProperty("jnativehook.lib.load", "false");
		System.load(new File(System.getProperty("user.dir"),"lib/JNativeHook.x86_64.dll").getAbsolutePath());
		GlobalScreen.registerNativeHook();
		LogUtils.println("JNativeHook loaded");
	}
}
