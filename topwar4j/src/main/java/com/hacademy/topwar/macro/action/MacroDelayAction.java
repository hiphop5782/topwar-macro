package com.hacademy.topwar.macro.action;

public class MacroDelayAction implements MacroAction{
	
	private double second = 1;
	
	public MacroDelayAction(double second) {
		this.second = second;
	}
	
	@Override
	public void doSomething() {
		try {
			Thread.sleep((long)(second * 1000L));
		}
		catch(Exception e) {}
	}
}
