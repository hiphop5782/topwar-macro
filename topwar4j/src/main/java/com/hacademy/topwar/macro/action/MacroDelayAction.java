package com.hacademy.topwar.macro.action;

import com.hacademy.topwar.ui.LogDialog;

public class MacroDelayAction implements MacroAction{
	
	private double second = 1;
	
	public MacroDelayAction(double second) {
		this.second = second;
	}
	
	public long getDuration() {
		return (long) (this.second * 1000L);
	}
	
	@Override
	public void doSomething() throws InterruptedException{
		long time = (long)(second * 1000L);
		long acc = 0;
		long before = System.currentTimeMillis();
		while(time > acc) {
			Thread.sleep(100);
			long now = System.currentTimeMillis();
			long diff = now - before;
			acc += diff;
			System.out.println(acc+"/"+time+" 휴식 진행중");
			before = now;
		}
	}
	
	@Override
	public String toString() {
		return second+"초 정지";
	}
	
}
