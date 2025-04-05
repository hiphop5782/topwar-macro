package com.hacademy.topwar.macro.action;

public class MacroDelayAction implements MacroAction{
	
	private double second = 1;
	
	public MacroDelayAction(double second) {
		System.out.println("delay create = " + second + "s");
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
		System.out.println(time+"ms 휴식 액션");
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
