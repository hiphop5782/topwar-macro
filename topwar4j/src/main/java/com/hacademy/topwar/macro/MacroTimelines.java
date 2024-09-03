package com.hacademy.topwar.macro;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class MacroTimelines {
	private List<MacroTimeline> list = new ArrayList<>();
	private long delayBetween = 400L;
	private boolean isPlaying = false;
	private Thread thread;
	public MacroTimelines() {}
	public MacroTimelines(MacroTimelinesListener listener) {
		this.listener = listener;
	}
	public void add(MacroTimeline timeline) {
		list.add(timeline);
	}
	public void play() {
		if(isPlaying) return;
		isPlaying = true;
		
		if(listener != null) listener.start(this);
		
		thread = new Thread(()->{
			int size = list.get(0).size();
			
			try {
				while(isPlaying) {
					if(listener != null) listener.cycleStart(this);
					for(int i=0; i < size; i++) {
						for(MacroTimeline timeline : list) {
							timeline.play(i);
							pause();
						}
					}
					if(listener != null) listener.cycleFinish(this);
				}
			}
			catch(Exception e) {}
			
			if(listener != null) listener.finish(this);
			isPlaying = false;
		});
		thread.setDaemon(true);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}
	public void pause() throws InterruptedException {
		long delay = (long)(Math.random() * 400L) + delayBetween - 200L;
		Thread.sleep(delay);
	}
	public void stop() {
		isPlaying = false;
		thread.interrupt();
	}
	public boolean playing() {
		return isPlaying;
	}
	
	//이벤트
	@Setter
	private MacroTimelinesListener listener;
}
