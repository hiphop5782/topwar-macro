package com.hacademy.topwar.macro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Data;
import lombok.Setter;

@Data
public class MacroTimelines implements Iterable<MacroTimeline> {
	private String name;
	private List<MacroTimeline> timelineList = new ArrayList<>();
	private List<Double> delayList = new ArrayList<>();
	private boolean seperate;
	public MacroTimelines(String name) {
		this.name = name;
		this.seperate = false;
	}
	public MacroTimelines(String name, boolean seperate) {
		this.name = name;
		this.seperate = seperate;
	}
	public void add(MacroTimeline timeline) {
		timelineList.add(timeline);
		delayList.add(0.25d);
	}
	public void add(MacroTimeline timeline, double delayAfter) {
		timelineList.add(timeline);
		delayList.add(delayAfter);
	}
	public MacroTimeline last() {
		return timelineList.isEmpty() ? null : timelineList.get(timelineList.size()-1);
	}
	public void playOnce() throws InterruptedException {
		play(1);
	}
	public void play(int count) throws InterruptedException {
		if(listener != null) listener.start(this);
		
		for(int k=0; k < count; k++) {
			if(seperate) {//독립 매크로
				for(int i=0; i < timelineList.size(); i++) {
					MacroTimeline timeline = timelineList.get(i);
					double delay = delayList.get(i);
					timeline.play(delay);
				}
			}
			else {//종속 매크로
				int size = 0;//가장 긴 행의 개수
				for(int i=0; i < timelineList.size(); i++) {
					if(timelineList.get(i).size() > size) {
						size = timelineList.get(i).size();
					}
				}
				
				for(int i=0; i < size; i++) {
					//i 위치에 대하여 모든 매크로를 반복
					for(int j=0; j < timelineList.size(); j++) {
						MacroTimeline timeline = timelineList.get(j);
						if(i < timeline.size()) {
							double delay = delayList.get(j);
							timeline.play(i, delay);
						}
					}
				}
				
			}
		}
		
		if(listener != null) listener.finish(this);
	}
	public void play() throws InterruptedException {
		while(true) {
			if(seperate) {//독립 매크로
				for(int i=0; i < timelineList.size(); i++) {
					MacroTimeline timeline = timelineList.get(i);
					double delay = delayList.get(i);
					timeline.play(delay);
				}
			}
			else {//종속 매크로
				int size = timelineList.get(0).size();
				for(int i=0; i < size; i++) {
					for(int j=0; j < timelineList.size(); j++) {
						MacroTimeline timeline = timelineList.get(j);
						double delay = delayList.get(i);
						timeline.play(i, delay);
					}
				}
			}
		}
	}
	public void pause(double second) throws InterruptedException {
		Thread.sleep((long)(second * 1000L));
	}
	
	public long getDuration() {
		long playMs = timelineList.stream()
				.map(timeline->timeline.getDuration())
				.reduce(0L, (acc,cur)->acc+cur);
		double delaySec = delayList.stream().map(v->v.doubleValue())
				.reduce(0d, (acc, cur)->acc+cur);
		return playMs + (long)(delaySec * 1000L);
	}
	public int size() {
		return timelineList.size();
	}
	
	//이벤트
	@Setter
	private MacroTimelinesListener listener;
	@Override
	public Iterator<MacroTimeline> iterator() {
		return timelineList.iterator();
	}
}
