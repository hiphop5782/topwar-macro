package com.hacademy.topwar.macro.action;

import com.hacademy.topwar.util.Keyboard;

public class MacroTypingAction implements MacroAction {
	private String text;
	public MacroTypingAction(String text) {
		this.text = text;
	}
	@Override
	public void doSomething() throws InterruptedException {
		Keyboard.create()
			.saveToClipboard(text).hold()
			.paste().hold()
			.enter();
	}
	@Override
	public long getDuration() {
		return 1000;
	}
	
	@Override
	public String toString() {
		return "텍스트 타이핑 = " + text;
	}

}
