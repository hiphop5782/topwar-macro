package com.hacademy.topwar.macro;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Data;

@Data
public class MacroTimelinesGroup {
	private List<MacroTimelines> timelinesList = new ArrayList<>();
	private MacroTimelinesListener listener;	
	private volatile boolean playing = false;
	private ExecutorService service;
	public MacroTimelinesGroup(MacroTimelinesListener listener) {
		this.listener = listener;
	}
	public void add(MacroTimelines timelines) {
		timelinesList.add(timelines);
		timelines.setListener(listener);
	}
	public void clear() {
		timelinesList.clear();
	}
	public int size() {
		return timelinesList.size();
	}
	public void playOnce() {
		play(1);
	}
	public void play(int count) {
		if(playing) return;
		if(timelinesList.isEmpty()) return;
		
		playing = true;
		
		service = Executors.newSingleThreadExecutor();
		service.submit(()->{
			if(listener != null) listener.start(null);
			
			try {
				for(MacroTimelines timelines : timelinesList) {
					if(listener != null) listener.cycleStart(timelines);
					timelines.play(count);
					if(listener != null) listener.cycleFinish(timelines);
				}
			}
			catch(Exception e) {
				System.out.println("Play {"+count+"} is interruted");
//				e.printStackTrace();
			}
			finally {
				playing = false;
			}
		});
	}
	public void play() {
		if(playing) return;
		if(timelinesList.isEmpty()) return;

		playing = true;
		
		service = Executors.newSingleThreadExecutor();
		service.submit(()->{
			try {
				for(MacroTimelines timelines : timelinesList) {
					timelines.play();
				}
			}
			catch(Exception e) {
				System.out.println("Play infinite is interruted");
			}
			finally {
				playing = false;
			}
		});
	}
	public void stop() {
		if(service != null) {
			service.shutdownNow();
		}
		if(listener != null) listener.finish(null);
	}
}
