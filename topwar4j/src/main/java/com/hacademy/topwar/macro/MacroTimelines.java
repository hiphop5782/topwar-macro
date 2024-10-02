package com.hacademy.topwar.macro;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Setter;

public class MacroTimelines {
	private List<MacroTimeline> list = new ArrayList<>();
	private List<Double> delayList = new ArrayList<>();
	private volatile boolean isPlaying = false;
	private ExecutorService service = Executors.newSingleThreadExecutor();
	public MacroTimelines() {}
	public MacroTimelines(MacroTimelinesListener listener) {
		this.listener = listener;
	}
	public void add(MacroTimeline timeline) {
		list.add(timeline);
		delayList.add(0.25d);
	}
	public void add(MacroTimeline timeline, double delayAfter) {
		list.add(timeline);
		delayList.add(delayAfter);
	}
	public void playOnce() {
		play(1);
	}
	public void play(int count) {
		if(isPlaying) return;
		isPlaying = true;
		
		if(listener != null) listener.start(this);
		
		service.submit(()->{
			int size = list.get(0).size();
			
			int acc = 0;
			
			try {
				while(isPlaying) {
					if(listener != null) listener.cycleStart(this);
					for(int i=0; i < size; i++) {
//						for(MacroTimeline timeline : list) {
						for(int k=0; k < list.size(); k++) {
							MacroTimeline timeline = list.get(k);
							if(!isPlaying) throw new InterruptedException();
							timeline.play(i);
							
							double delay = delayList.get(k);
							if(delay > 0d) {
								pause(delay);
							}
						}
					}
					if(listener != null) listener.cycleFinish(this);
					if(++acc == count) break;
				}
			}
			catch(Exception e) {
				System.out.println("Count Macro Interrupted - "+e.getMessage());
//				e.printStackTrace();
			}
			
			if(listener != null) listener.finish(this);
			isPlaying = false;
		});
	}
	public void play() {
		if(isPlaying) return;
		isPlaying = true;
		
		if(listener != null) listener.start(this);
		
		service.submit(()->{
			int size = list.get(0).size();
			
			try {
				while(isPlaying) {
					if(listener != null) listener.cycleStart(this);
					for(int i=0; i < size; i++) {
						for(MacroTimeline timeline : list) {
							if(!isPlaying) throw new InterruptedException();
							timeline.play(i);
//							pause();
						}
					}
					if(listener != null) listener.cycleFinish(this);
				}
			}
			catch(Exception e) {
				System.out.println("Macro Interrupted - "+e.getMessage());
			}
			
			if(listener != null) listener.finish(this);
			isPlaying = false;
		});
	}
	public void pause(double second) throws InterruptedException {
		Thread.sleep((long)(second * 1000L));
	}
	public void stop() {
		isPlaying = false;
		service.shutdownNow();
	}
	public boolean playing() {
		return isPlaying;
	}
	
	//이벤트
	@Setter
	private MacroTimelinesListener listener;
}
