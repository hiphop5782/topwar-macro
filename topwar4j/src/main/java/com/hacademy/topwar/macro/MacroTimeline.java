package com.hacademy.topwar.macro;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.hacademy.topwar.macro.action.MacroAction;
import com.hacademy.topwar.macro.action.MacroMouseAction;
import com.hacademy.topwar.macro.action.MacroMouseActionType;

public class MacroTimeline {
	
	private List<MacroAction> actionList = new ArrayList<>();
	private boolean playing = false;
	
	private MacroActionListener listener;
	public void setListener(MacroActionListener listener) {
		this.listener = listener;
	}
	
	private long delayBetweenAction = 1000L;
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
	public void play() {
		if(actionList == null || actionList.size() == 0) return;
		playing = true;
		for(int i=0; i < actionList.size(); i++) {
			if(playing == false) {
				return;
			}
			
			MacroAction action = actionList.get(i);
			action.doSomething();
			if(listener != null) {
				listener.done(this, i);
			}
			pause();
		}
		playing = false;
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
	
	public static MacroTimeline getDarkforceMacro(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(new MacroMouseAction(basePoint.x + 87, basePoint.y + 665, MacroMouseActionType.CLICK));//적군 검색
		timeline.add(new MacroMouseAction(basePoint.x + 109, basePoint.y + 317, MacroMouseActionType.CLICK));//적군
		timeline.add(new MacroMouseAction(basePoint.x + 131, basePoint.y + 437, MacroMouseActionType.CLICK));//암흑 오딘
		timeline.add(new MacroMouseAction(basePoint.x + 250, basePoint.y + 600, MacroMouseActionType.CLICK));//레벨선택
		timeline.add(new MacroMouseAction(basePoint.x + 249, basePoint.y + 652, MacroMouseActionType.CLICK));//검색
		timeline.add(new MacroMouseAction(basePoint.x + 246, basePoint.y + 353, MacroMouseActionType.CLICK));//유닛선택
		
		if(status.getDarkforceAttackCount() == 1) {//1회 공격
			timeline.add(new MacroMouseAction(basePoint.x + 319, basePoint.y + 260, MacroMouseActionType.CLICK));
		}
		else {//5회 공격
			timeline.add(new MacroMouseAction(basePoint.x + 184, basePoint.y + 260, MacroMouseActionType.CLICK));
		}
		
		timeline.add(new MacroMouseAction(basePoint.x + 104, basePoint.y + 19, MacroMouseActionType.CLICK));//체력충전 창열기
		timeline.add(new MacroMouseAction(basePoint.x + 155, basePoint.y + 378, MacroMouseActionType.CLICK));//물약선택
		timeline.add(new MacroMouseAction(basePoint.x + 257, basePoint.y + 486, MacroMouseActionType.CLICK));//물약사용
		timeline.add(new MacroMouseAction(basePoint.x + 412, basePoint.y + 219, MacroMouseActionType.CLICK));//체력충전 창닫기
		
		//부대선택
		switch(status.getDarkforceMarchCount()) {
		case 1:
			timeline.add(new MacroMouseAction(basePoint.x + 286, basePoint.y + 591, MacroMouseActionType.CLICK));//1번부대선택
			break;
		case 2:
			timeline.add(new MacroMouseAction(basePoint.x + 315, basePoint.y + 591, MacroMouseActionType.CLICK));//2번부대선택
			break;
		case 3:
			timeline.add(new MacroMouseAction(basePoint.x + 343, basePoint.y + 591, MacroMouseActionType.CLICK));//3번부대선택
			break;
		case 4:
			timeline.add(new MacroMouseAction(basePoint.x + 374, basePoint.y + 591, MacroMouseActionType.CLICK));//4번부대선택
			break;
		case 5:
			timeline.add(new MacroMouseAction(basePoint.x + 397, basePoint.y + 591, MacroMouseActionType.CLICK));//5번부대선택
			break;
		case 6:
			timeline.add(new MacroMouseAction(basePoint.x + 424, basePoint.y + 591, MacroMouseActionType.CLICK));//6번부대선택
			break;	
		case 7:
			timeline.add(new MacroMouseAction(basePoint.x + 453, basePoint.y + 591, MacroMouseActionType.CLICK));//7번부대선택
			break;
		case 8:
			timeline.add(new MacroMouseAction(basePoint.x + 485, basePoint.y + 591, MacroMouseActionType.CLICK));//8번부대선택
			break;
		}
		
		//출정
		timeline.add(new MacroMouseAction(basePoint.x + 246, basePoint.y + 288, MacroMouseActionType.CLICK));
		return timeline;
	}
}







