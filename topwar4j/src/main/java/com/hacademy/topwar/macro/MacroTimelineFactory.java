package com.hacademy.topwar.macro;

import java.awt.Point;

import com.hacademy.topwar.macro.action.MacroDelayAction;
import com.hacademy.topwar.macro.action.MacroMouseAction;
import com.hacademy.topwar.macro.action.MacroMouseActionType;

public class MacroTimelineFactory {
	public static MacroTimeline getDarkforceMacro(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(new MacroMouseAction(basePoint.x + 87, basePoint.y + 665, MacroMouseActionType.CLICK));//적군 검색
		timeline.add(new MacroMouseAction(basePoint.x + 109, basePoint.y + 317, MacroMouseActionType.CLICK));//적군
		timeline.add(new MacroMouseAction(basePoint.x + 131, basePoint.y + 437, MacroMouseActionType.CLICK));//암흑 오딘
		
		//레벨선택은 랜덤하게 설정(98~102)
		//timeline.add(new MacroMouseAction(basePoint.x + 250, basePoint.y + 600, MacroMouseActionType.CLICK));//레벨선택
		int offset = (int)(Math.random() * 101) + 200;
		timeline.add(new MacroMouseAction(basePoint.x + offset, basePoint.y + 600, MacroMouseActionType.CLICK));//레벨선택
		
		timeline.add(new MacroMouseAction(basePoint.x + 249, basePoint.y + 652, MacroMouseActionType.CLICK));//검색

		//매크로 개수에 맞게 대기(4~6초)
		int delay = (int)(Math.random() * 2) + 4;
		timeline.add(new MacroDelayAction(delay + Math.random()));
		
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
		
		//대기 - 매크로 개수별로 다르게...
		if(status.getDarkforceAttackCount() == 1) {
			delay = (int)(Math.random() * 2) + 29;
			timeline.add(new MacroDelayAction(delay + Math.random()));
		}
		else {
			delay = (int)(Math.random() * 10) + 295;
			timeline.add(new MacroDelayAction(delay + Math.random()));
		}
		
		return timeline;
	}
}
