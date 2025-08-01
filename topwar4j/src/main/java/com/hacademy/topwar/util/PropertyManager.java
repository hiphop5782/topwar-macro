package com.hacademy.topwar.util;

import com.hacademy.topwar.macro.MacroStatus;
import com.hacademy.topwar.ui.LogDialog;
import com.hacademy.topwar.ui.WindowStatus;

import lombok.Getter;

public class PropertyManager {
	@Getter
	private static MacroStatus macroStatus;
	@Getter
	private static WindowStatus windowStatus;
	static {
		System.out.println("* property manager 실행");
		macroStatus = JsonConfigUtil.load(MacroStatus.class);
		windowStatus = JsonConfigUtil.load(WindowStatus.class);
	}
	public static void saveMacroStatus() {
		JsonConfigUtil.save(macroStatus);
	}
	public static void saveWindowStatus() {
		JsonConfigUtil.save(windowStatus);
	}
}
