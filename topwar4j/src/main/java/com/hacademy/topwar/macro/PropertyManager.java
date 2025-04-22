package com.hacademy.topwar.macro;

import com.hacademy.topwar.ui.WindowStatus;
import com.hacademy.topwar.util.JsonConfigUtil;

import lombok.Getter;

public class PropertyManager {
	@Getter
	private static MacroStatus macroStatus;
	@Getter
	private static WindowStatus windowStatus;
	static {
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
