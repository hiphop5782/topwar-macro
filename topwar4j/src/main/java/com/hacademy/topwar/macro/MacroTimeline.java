package com.hacademy.topwar.macro;

import java.util.ArrayList;
import java.util.List;

import com.hacademy.topwar.macro.action.MacroAction;
import com.hacademy.topwar.ui.LogDialog;
import com.hacademy.topwar.util.PropertyManager;

import lombok.Setter;

public class MacroTimeline {
	
	private List<MacroAction> actionList = new ArrayList<>();
	private List<Double> delayList = new ArrayList<>();
	
	private MacroActionListener listener;
	public void setListener(MacroActionListener listener) {
		this.listener = listener;
	}
	
	@Setter
	private double delayBetweenAction = 0.3d;
	
	public MacroTimeline() {}
	public MacroTimeline(double delayBetweenAction) {
		this.setDelayBetweenAction(delayBetweenAction);
	}	
	
	public void add(MacroAction action) {
		add(action, 0d);
		
	}
	public void add(MacroAction action, double afterDelay) {
		actionList.add(action);
//		actionList.add(new MacroDelayAction(delayBetweenAction));
		delayList.add(afterDelay);
		if(listener != null) {
			listener.add(this);
		}
	}
	public void add(MacroTimeline timeline) {
		actionList.addAll(timeline.actionList);
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
	public void play(double delaySecond) throws InterruptedException {
		if(actionList == null || actionList.size() == 0) return;
		for(int i=0; i < actionList.size(); i++) {
			play(i);
			if(delaySecond > 0d) {
				pause(delaySecond);
			}
		}
		if(listener != null) {
			listener.done(this, 0);
		}
	}
	public void play(int index) throws InterruptedException {
		MacroAction action = actionList.get(index);
		System.out.println("<"+action.getClass().getSimpleName()+"> " + action + "(no delay)");
		action.doSomething();
		if(listener != null) {
			listener.done(this, index);
		}
	}
	public void play(int index, double delaySecond) throws InterruptedException {
		MacroAction action = actionList.get(index);
		System.out.println("<"+action.getClass().getSimpleName()+"> " + action + "(delay "+delaySecond+"s)");
		action.doSomething();
		if(listener != null) {
			listener.done(this, index);
		}
		if(delaySecond > 0d) {
			pause(delaySecond);
		}
	}
	private void pause(double delaySecond) throws InterruptedException {
		//실행되는 화면이 2개 이하일 때는 강제로 딜레이 보정
		if(PropertyManager.getMacroStatus().getActiveScreen() <= 2) {
			delaySecond = 0.75d;
		}
		System.out.println("├──── pause : " + delaySecond + "s ──┤");
		Thread.sleep((long)(delaySecond * 1000L));
	}
	public void stop() {
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
	
	public long getDuration() {
		return actionList.stream()
				.map(action->action.getDuration())
				.reduce(0L, (acc, cur)->acc+cur);
	}
	
}







