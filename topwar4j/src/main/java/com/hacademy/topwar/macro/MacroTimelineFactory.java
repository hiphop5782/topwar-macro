package com.hacademy.topwar.macro;

import java.awt.Point;

import com.hacademy.topwar.macro.action.MacroAction;
import com.hacademy.topwar.macro.action.MacroDelayAction;
import com.hacademy.topwar.macro.action.MacroMouseAction;
import com.hacademy.topwar.macro.action.MacroMouseActionType;

public class MacroTimelineFactory {
	public static MacroTimeline getDarkforceOnceMacro(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(clickSearchButton(status, basePoint));//적군 검색(+다른창닫기)
		timeline.add(clickSearchButton(status, basePoint));//적군 검색
		timeline.add(clickDarkforceButton(status, basePoint));//적군
		timeline.add(selectDarkforceTypeButton(status, basePoint));//암흑 오딘
		
		//레벨선택은 랜덤하게 설정(98~102)
		timeline.add(selectRandomDarkforce(status, basePoint));//레벨선택
		
		timeline.add(selectEnemySearchConfirmButton(status, basePoint));//검색

		//매크로 개수에 맞게 대기(4~6초)
		timeline.add(randomDelay(status, 4, 6));
		
		//공격
		timeline.add(selectEnemy(status, basePoint));//유닛선택
		
		if(status.getDarkforceAttackCount() == 1) {//1회 공격
			timeline.add(topRightAttackButton(status, basePoint));
		}
		else {//5회 공격
			timeline.add(topLeftAttackButton(status, basePoint));
		}
		
		//물약 사용(option)
		if(status.isPotion()) {
			timeline.add(choiceVitPotion(status, basePoint));
			timeline.add(useVitPotion(status, basePoint));
			timeline.add(closeVitPotionWindow(status, basePoint));
		}
		else {
			timeline.add(clickRestArea(status, basePoint));
		}
		
		//다시 공격
		timeline.add(selectEnemy(status, basePoint));//유닛선택
		
		if(status.getDarkforceAttackCount() == 1) {//1회 공격
			timeline.add(topRightAttackButton(status, basePoint));
		}
		else {//5회 공격
			timeline.add(topLeftAttackButton(status, basePoint));
		}
		
		//부대선택
		timeline.add(selectMarchNumber(status, basePoint));
		
		//출정
		timeline.add(sendMarchButton(status, basePoint));
		
		//대기 없음
		return timeline;
	}
	public static MacroTimeline getDarkforceLoopMacro(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = getDarkforceOnceMacro(status, basePoint);
		
		//대기 - 매크로 개수별로 다르게...
		if(status.getDarkforceAttackCount() == 1) {
			timeline.add(randomDelay(status, 30, 35));
		}
		else {
			timeline.add(randomDelay(status, 295, 310));
		}
		
		return timeline;
	}
	
	public static MacroTimeline getTerror4kOnceMacro(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(clickSearchButton(status, basePoint));//적군 검색(+다른창닫기)
		timeline.add(clickSearchButton(status, basePoint));//적군 검색
		timeline.add(clickRallyButton(status, basePoint));//집결
		timeline.add(selectTerror4kButton(status, basePoint));//테러-4K
		
		//레벨선택
		timeline.add(selectTerror4kLevelButton(status, basePoint));
		
		timeline.add(selectEnemySearchConfirmButton(status, basePoint));//검색

		//매크로 개수에 맞게 대기(4~6초)
		timeline.add(randomDelay(status, 4, 6));
		
		//공격
		timeline.add(selectEnemy(status, basePoint));//유닛선택
		
		if(status.isTerror4kManual()) {//수동 공격
			timeline.add(bottomRightAttackButton(status, basePoint));
		}
		else {//집결 공격
			timeline.add(bottomLeftAttackButton(status, basePoint));
		}
		
		//물약 사용(option)
		if(status.isPotion()) {
			timeline.add(choiceVitPotion(status, basePoint));
			timeline.add(useVitPotion(status, basePoint));
			timeline.add(closeVitPotionWindow(status, basePoint));
		}
		else {
			timeline.add(clickRestArea(status, basePoint));
		}
		
		//다시 공격
		timeline.add(selectEnemy(status, basePoint));//유닛선택
		
		if(status.isTerror4kManual()) {//수동 공격
			timeline.add(bottomRightAttackButton(status, basePoint));
		}
		else {//집결 공격
			timeline.add(bottomLeftAttackButton(status, basePoint));
		}
		
		//부대선택
		timeline.add(selectMarchNumber(status, basePoint));
		
		//출정
		timeline.add(sendMarchButton(status, basePoint));
		
		//대기 없음
		return timeline;
	}
	
	public static MacroTimeline getTerror4kLoopMacro(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = getTerror4kOnceMacro(status, basePoint);
		
		//대기 90~100초
		timeline.add(randomDelay(status, 90, 100));
		
		return timeline;
	}
	
	//부대선택
	private static MacroAction selectMarchNumber(MacroStatus status, Point basePoint) {
			return switch(status.getDarkforceMarchNumber()) {
			case 1 -> new MacroMouseAction(basePoint.x + 286, basePoint.y + 591);//1번부대선택
			case 2 -> new MacroMouseAction(basePoint.x + 315, basePoint.y + 591);//2번부대선택
			case 3 -> new MacroMouseAction(basePoint.x + 343, basePoint.y + 591);//3번부대선택
			case 4 -> new MacroMouseAction(basePoint.x + 374, basePoint.y + 591);//4번부대선택
			case 5 -> new MacroMouseAction(basePoint.x + 397, basePoint.y + 591);//5번부대선택
			case 6 -> new MacroMouseAction(basePoint.x + 424, basePoint.y + 591);//6번부대선택
			case 7 -> new MacroMouseAction(basePoint.x + 453, basePoint.y + 591);//7번부대선택
			case 8 -> new MacroMouseAction(basePoint.x + 485, basePoint.y + 591);
			default -> throw new IllegalArgumentException("Unexpected value: " + status.getDarkforceMarchNumber());//8번부대선택
			};
	}
	private static MacroAction topLeftAttackButton(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 184, basePoint.y + 260);
	}
	private static MacroAction topRightAttackButton(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 319, basePoint.y + 260);
	}
	private static MacroAction bottomLeftAttackButton(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 184, basePoint.y + 564);
	}
	private static MacroAction bottomRightAttackButton(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 319, basePoint.y + 564);
	}
	private static MacroAction sendMarchButton(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 246, basePoint.y + 288);
	}
	private static MacroAction openVitPotionWindow(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 104, basePoint.y + 19);//체력충전 창열기
	}
	private static MacroAction choiceVitPotion(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 155, basePoint.y + 378);//물약선택
	}
	private static MacroAction useVitPotion(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 257, basePoint.y + 486);//물약사용
	}
	private static MacroAction closeVitPotionWindow(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 412, basePoint.y + 219);//체력충전 창닫기
	}
	private static MacroAction clickRestArea(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 250, basePoint.y + 170);
	}
	private static MacroAction clickSearchButton(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 87, basePoint.y + 665);
	}
	private static MacroAction selectRandomDarkforce(MacroStatus status, Point basePoint) {
		return selectRandomDarkforce(status, basePoint, 4, 8);
	}
	private static MacroAction selectRandomDarkforce(MacroStatus status, Point basePoint, int begin, int end) {
		int range = end - begin + 1;
		int level = (int)(Math.random() * range) + begin;
		return selectDarkforceLevelButton(status, basePoint, level);
	}
	private static MacroAction selectRandomWarhammer4k(MacroStatus status, Point basePoint) {
		int level = (int)(Math.random() * 4) + 5;//5,6,7,8
		return selectWarhammer4kLevelButton(status, basePoint, level);
	}
	private static MacroAction clickDarkforceButton(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 109, basePoint.y + 317);
	}
	private static MacroAction clickRallyButton(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 300, basePoint.y + 317);
	}
	private static MacroAction selectDarkforceTypeButton(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 131, basePoint.y + 437);//암흑 오딘
	}
	private static MacroAction selectTerror4kButton(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 257, basePoint.y + 437);
	}
	private static MacroAction selectDarkforceLevelButton(MacroStatus status, Point basePoint, int level) {
		return switch(level) {
		case 1 -> new MacroMouseAction(basePoint.x + 170, basePoint.y + 597);
		case 2 -> new MacroMouseAction(basePoint.x + 186, basePoint.y + 597);
		case 3 -> new MacroMouseAction(basePoint.x + 203, basePoint.y + 597);
		case 4 -> new MacroMouseAction(basePoint.x + 218, basePoint.y + 597);
		case 5 -> new MacroMouseAction(basePoint.x + 233, basePoint.y + 597);
		case 6 -> new MacroMouseAction(basePoint.x + 250, basePoint.y + 597);
		case 7 -> new MacroMouseAction(basePoint.x + 266, basePoint.y + 597);
		case 8 -> new MacroMouseAction(basePoint.x + 281, basePoint.y + 597);
		case 9 -> new MacroMouseAction(basePoint.x + 298, basePoint.y + 597);
		case 10 -> new MacroMouseAction(basePoint.x + 312, basePoint.y + 597);
		case 11 -> new MacroMouseAction(basePoint.x + 330, basePoint.y + 597);
		default -> throw new IllegalArgumentException("Unexpected value: " + level);
		};
	}
	private static MacroAction selectWarhammer4kLevelButton(MacroStatus status, Point basePoint, int level) {
		return switch(level) {
		case 1 -> new MacroMouseAction(basePoint.x + 170, basePoint.y + 597);
		case 2 -> new MacroMouseAction(basePoint.x + 193, basePoint.y + 597);
		case 3 -> new MacroMouseAction(basePoint.x + 216, basePoint.y + 597);
		case 4 -> new MacroMouseAction(basePoint.x + 239, basePoint.y + 597);
		case 5 -> new MacroMouseAction(basePoint.x + 262, basePoint.y + 597);
		case 6 -> new MacroMouseAction(basePoint.x + 285, basePoint.y + 597);
		case 7 -> new MacroMouseAction(basePoint.x + 308, basePoint.y + 597);
		case 8 -> new MacroMouseAction(basePoint.x + 330, basePoint.y + 597);
		default -> throw new IllegalArgumentException("Unexpected value: " + level);
		};
	}
	private static MacroAction selectTerror4kLevelButton(MacroStatus status, Point basePoint) {
		return switch(status.getTerror4kLevel()) {
		case 1 -> new MacroMouseAction(basePoint.x + 170, basePoint.y + 597);
		case 2 -> new MacroMouseAction(basePoint.x + 210, basePoint.y + 597);
		case 3 -> new MacroMouseAction(basePoint.x + 250, basePoint.y + 597);
		case 4 -> new MacroMouseAction(basePoint.x + 290, basePoint.y + 597);
		case 5 -> new MacroMouseAction(basePoint.x + 330, basePoint.y + 597);
		default -> throw new IllegalArgumentException("Unexpected value: " + status.getTerror4kLevel());
		};
	}
	private static MacroAction selectEnemySearchConfirmButton(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 249, basePoint.y + 652);
	}
	private static MacroAction randomDelay(MacroStatus status, double begin, double end) {
		double range = end - begin;
		double delay = Math.random() * range + begin;
		return new MacroDelayAction(delay / status.getScreenList().size());
	}
	private static MacroAction selectEnemy(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint.x + 246, basePoint.y + 353);
	}
}





