package com.hacademy.topwar.macro;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.hacademy.topwar.macro.action.MacroAction;
import com.hacademy.topwar.macro.action.MacroDelayAction;
import com.hacademy.topwar.macro.action.MacroMouseAction;
import com.hacademy.topwar.macro.action.MacroMouseActionType;

public class MacroTimeline {
	
	private List<MacroAction> actionList = new ArrayList<>();
	private boolean playing = false;
	
	private MacroActionListener listener;
	public void setListener(MacroActionListener listener) {
		this.listener = listener;
	}
	
	private long delayBetweenAction = 300L;
	public void setDelayBetweenAction(long delayBetweenAction) {
		if(delayBetweenAction < 10) return;
		this.delayBetweenAction = delayBetweenAction;
	}
	public MacroTimeline() {}
	public MacroTimeline(long delayBetweenAction) {
		this.setDelayBetweenAction(delayBetweenAction);
	}	
	
	public boolean isPlaying() {
		return this.playing;
	}
	
	public void add(MacroAction action) {
		actionList.add(action);
		if(listener != null) {
			listener.add(this);
		}
	}
	public void add(List<MacroAction> actions) {
		actionList.addAll(actions);
		if(listener != null) {
			listener.add(this);
		}
	}
	public void remove(MacroAction action) {
		actionList.remove(action);
		if(listener != null) {
			listener.remove(this);
		}
	}
	public void remove(int index) {
		actionList.remove(index);
		if(listener != null) {
			listener.remove(this);
		}
	}
	public void play() throws InterruptedException {
		if(actionList == null || actionList.size() == 0) return;
		playing = true;
		for(int i=0; i < actionList.size(); i++) {
			if(playing == false) {
				return;
			}
			
			play(i);
			pause();
		}
		playing = false;
	}
	public void play(int index) throws InterruptedException {
		MacroAction action = actionList.get(index);
		action.doSomething();
		if(listener != null) {
			listener.done(this, index);
		}
	}
	private void pause() {
		try {
			Thread.sleep(delayBetweenAction);
		}
		catch(Exception e) {}
	}
	public void stop() {
		playing = false;
		Thread.currentThread().interrupt();
	}
	
	public String[] toArray() {
		String[] result = new String[actionList.size()];
		for(int i=0; i < result.length; i++) {
			MacroAction action = actionList.get(i);
			if(action == null) continue;
			result[i] = action.toString();
		}
		return result;
	}
	public void clear() {
		actionList.clear();
		if(listener != null) {
			listener.clear();
		}
	}
	public int size() {
		return actionList.size();
	}
	public boolean isEmpty() {
		return size() == 0;
	}
	public MacroAction getLast() {
		return actionList.isEmpty() ? null : actionList.get(actionList.size()-1);
	}
	
}







