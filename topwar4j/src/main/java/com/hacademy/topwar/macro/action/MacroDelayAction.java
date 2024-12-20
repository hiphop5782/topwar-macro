package com.hacademy.topwar.macro.action;

public class MacroDelayAction implements MacroAction{
	
	private double second = 1;
	
	public MacroDelayAction(double second) {
		this.second = second;
	}
	
	@Override
	public void doSomething() throws InterruptedException{
		long time = (long)(second * 1000L);
		long acc = 0;
		long before = System.currentTimeMillis();
		while(time > acc) {
			Thread.sleep(100);
			long now = System.currentTimeMillis();
			acc += now - before;
			before = now;
		}
	}
	
	@Override
	public String toString() {
		return second+"초 정지";
	}
	
}
