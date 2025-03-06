package com.hacademy.topwar.macro.action;

public interface MacroAction {
	void doSomething() throws InterruptedException;
	long getDuration();
}
