package com.hacademy.topwar;

import com.hacademy.topwar.macro.MacroStatus;
import com.hacademy.topwar.util.JsonConfigUtil;

public class Test22프로퍼티관리자 {
	public static void main(String[] args) {
		MacroStatus status = JsonConfigUtil.load(MacroStatus.class);	
		System.out.println(status);
	}
}
