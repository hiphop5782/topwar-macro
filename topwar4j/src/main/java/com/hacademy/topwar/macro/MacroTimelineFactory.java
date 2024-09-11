package com.hacademy.topwar.macro;

import java.awt.Point;

import com.hacademy.topwar.macro.action.MacroAction;
import com.hacademy.topwar.macro.action.MacroDelayAction;
import com.hacademy.topwar.macro.action.MacroMouseAction;
import com.hacademy.topwar.macro.action.MacroMouseActionType;

public class MacroTimelineFactory {
	public static MacroTimeline 암흑매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(적군검색버튼클릭(status, basePoint));//적군 검색(+다른창닫기)
		timeline.add(적군검색버튼클릭(status, basePoint));//적군 검색
		timeline.add(적군탭선택(status, basePoint));//적군
		timeline.add(암흑오딘유닛선택(status, basePoint));//암흑 오딘
		
		//레벨선택은 랜덤하게 설정(98~102)
		timeline.add(랜덤암흑유닛선택(status, basePoint));//레벨선택
		
		timeline.add(적군검색(status, basePoint));//검색

		//매크로 개수에 맞게 대기(4~6초)
		timeline.add(randomDelay(status, 4, 6));
		
		//공격
		timeline.add(적선택(status, basePoint));//유닛선택
		
		if(status.getDarkforceAttackCount() == 1) {//1회 공격
			timeline.add(상단창우측공격버튼(status, basePoint));
		}
		else {//5회 공격
			timeline.add(상단창좌측공격버튼(status, basePoint));
		}
		
		//물약 사용(option)
		if(status.isPotion()) {
			timeline.add(체력물약선택(status, basePoint));
			timeline.add(체력물약사용(status, basePoint));
			timeline.add(체력충전창닫기(status, basePoint));
		}
		else {
			timeline.add(빈공간선택(status, basePoint));
		}
		
		//다시 공격
		timeline.add(적선택(status, basePoint));//유닛선택
		
		if(status.getDarkforceAttackCount() == 1) {//1회 공격
			timeline.add(상단창우측공격버튼(status, basePoint));
		}
		else {//5회 공격
			timeline.add(상단창좌측공격버튼(status, basePoint));
		}
		
		//부대선택
		timeline.add(부대번호선택(status, basePoint));
		
		//출정
		timeline.add(출정버튼클릭(status, basePoint));
		
		//대기 없음
		return timeline;
	}
	public static MacroTimeline 암흑반복매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = 암흑매크로(status, basePoint);
		
		//대기 - 매크로 개수별로 다르게...
		if(status.getDarkforceAttackCount() == 1) {
			timeline.add(randomDelay(status, 30, 35));
		}
		else {
			timeline.add(randomDelay(status, 295, 310));
		}
		
		return timeline;
	}
	
	public static MacroTimeline 테러매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(적군검색버튼클릭(status, basePoint));//적군 검색(+다른창닫기)
		timeline.add(적군검색버튼클릭(status, basePoint));//적군 검색
		timeline.add(집결탭선택(status, basePoint));//집결
		timeline.add(테러선택(status, basePoint));//테러-4K
		
		//레벨선택
		timeline.add(테러레벨선택(status, basePoint));
		
		timeline.add(적군검색(status, basePoint));//검색

		//매크로 개수에 맞게 대기(4~6초)
		timeline.add(randomDelay(status, 4, 6));
		
		//공격
		timeline.add(적선택(status, basePoint));//유닛선택
		
		if(status.isTerror4kManual()) {//수동 공격
			timeline.add(하단창우측공격버튼(status, basePoint));
		}
		else {//집결 공격
			timeline.add(하단창좌측공격버튼(status, basePoint));
		}
		
		//물약 사용(option)
		if(status.isPotion()) {
			timeline.add(체력물약선택(status, basePoint));
			timeline.add(체력물약사용(status, basePoint));
			timeline.add(체력충전창닫기(status, basePoint));
		}
		else {
			timeline.add(빈공간선택(status, basePoint));
		}
		
		//다시 공격
		timeline.add(적선택(status, basePoint));//유닛선택
		
		if(status.isTerror4kManual()) {//수동 공격
			timeline.add(하단창우측공격버튼(status, basePoint));
		}
		else {//집결 공격
			timeline.add(하단창좌측공격버튼(status, basePoint));
		}
		
		//부대선택
		timeline.add(부대번호선택(status, basePoint));
		
		//출정
		timeline.add(출정버튼클릭(status, basePoint));
		
		//대기 없음
		return timeline;
	}
	
	public static MacroTimeline 테러반복매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = 테러매크로(status, basePoint);
		
		//대기 90~100초
		timeline.add(randomDelay(status, 90, 100));
		
		return timeline;
	}
	public static MacroTimeline 사판훈련매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(일일임무버튼(status, basePoint));
		timeline.add(randomDelay(status, 3, 5));
		timeline.add(사판훈련(status, basePoint));
		timeline.add(사판훈련도전(status, basePoint));
		timeline.add(randomDelay(status, 1, 2));
		timeline.add(부대번호선택(status, basePoint, 1));
		timeline.add(출정버튼클릭(status, basePoint));
		
		for(int i=0; i < 5; i++) {
			timeline.add(randomDelay(status, 5, 6));
			timeline.add(전투스킵(status, basePoint));
			timeline.add(randomDelay(status, 3, 5));
			timeline.add(사판훈련재도전(status, basePoint));
		}
		
		timeline.add(randomDelay(status, 2, 3));
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 무료보석수집매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(패키지상점(status, basePoint));
		timeline.add(주간카드탭(status, basePoint));
		timeline.add(randomDelay(status, 1, 2));
		
		for(int i=0; i < 20; i++) {
			timeline.add(주간카드무료다이아수령(status, basePoint));
			timeline.add(randomDelay(status, 16, 18));
		}
		
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 고급모집2회매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(영웅메뉴(status, basePoint));
		timeline.add(영웅모집(status, basePoint));
		timeline.add(randomDelay(status, 1, 2));
		
		for(int i=0; i < 2; i++) {
			//3회 누르거나 딜레이를 줘야 다시 원래화면으로 옴
			timeline.add(모집1회(status, basePoint));
			//timeline.add(모집1회(status, basePoint));//다중화면에서 부적합
			timeline.add(randomDelay(status, 5, 6));//다중화면에서 적합
			timeline.add(모집1회(status, basePoint));
		}
		
		timeline.add(randomDelay(status, 1, 2));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline VIP보상받기매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(VIP버튼선택(status, basePoint));
		timeline.add(일일VIP보상클릭(status, basePoint));
		
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 장바구니매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(VIP버튼선택(status, basePoint));
		timeline.add(VIP상점버튼클릭(status, basePoint));
		timeline.add(장바구니탭선택(status, basePoint));
		timeline.add(장바구니빠른교환클릭(status, basePoint));
		timeline.add(장바구니확인버튼(status, basePoint));
		
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 미지의작전매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(일일임무버튼(status, basePoint));
		timeline.add(randomDelay(status, 1, 2));
		timeline.add(미지의작전이동(status, basePoint));
		timeline.add(미지의작전보상버튼(status, basePoint));
		timeline.add(미지의작전보상수령(status, basePoint));
		
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 원정탐험매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(일일임무버튼(status, basePoint));
		timeline.add(원정탐험으로휠이동(status, basePoint));
		timeline.add(원정탐험선택(status, basePoint));
		
		timeline.add(randomDelay(status, 3, 5));
		
		timeline.add(원정탐험보상클릭(status, basePoint));
		timeline.add(원정탐험보상수령(status, basePoint));
		
		timeline.add(원정탐험빠른전투클릭(status, basePoint));
		timeline.add(원정탐험빠른전투시작(status, basePoint));
		timeline.add(randomDelay(status, 3, 5));
		timeline.add(원정탐험빠른전투스킵(status, basePoint));
		timeline.add(randomDelay(status, 3, 5));
		timeline.add(원정탐험보상수령(status, basePoint));
		
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 섬대작전매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(일일임무버튼(status, basePoint));
		timeline.add(섬대작전으로이동(status, basePoint));
		timeline.add(섬대작전선택(status, basePoint));
		
		for(int i=0; i < 2; i++) {
			timeline.add(randomDelay(status, 2, 3));
			timeline.add(섬대작전리셋(status, basePoint));
			timeline.add(randomDelay(status, 2, 3));
			timeline.add(섬대작전소탕(status, basePoint));
			timeline.add(섬대작전소탕시작(status, basePoint));
			timeline.add(randomDelay(status, 2, 3));
			timeline.add(섬대작전소탕완료(status, basePoint));
		}
		timeline.add(randomDelay(status, 2, 3));
		
		timeline.add(섬대작전나가기(status, basePoint));
		
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 패키지무료보상매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(패키지상점(status, basePoint));
		timeline.add(특별패키지탭(status, basePoint));
		timeline.add(특별패키지무료보상클릭(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 일일매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		if(status.isDailyVipReward())
			timeline.add(VIP보상받기매크로(status, basePoint));
		if(status.isDailyBasketReward())
			timeline.add(장바구니매크로(status, basePoint));
		if(status.isDailySpecialReward()) 
			timeline.add(패키지무료보상매크로(status, basePoint));
		if(status.isDailyGemReward())
			timeline.add(무료보석수집매크로(status, basePoint));
		if(status.isDailyHeavyTrooperReward())
			timeline.add(미지의작전매크로(status, basePoint));
		if(status.isDailySandTraning())
			timeline.add(사판훈련매크로(status, basePoint));
		if(status.isDailyExpeditionBase())
			timeline.add(원정탐험매크로(status, basePoint));
		if(status.isDailyIslandBattle())
			timeline.add(섬대작전매크로(status, basePoint));
		if(status.isDailyAdvancedIncruit())
			timeline.add(고급모집2회매크로(status, basePoint));
		
		return timeline;
	}
	
	//부대선택
	private static MacroAction 부대번호선택(MacroStatus status, Point basePoint, int number) {
		return switch(number) {
		case 1 -> new MacroMouseAction(basePoint, 286, 591);//1번부대선택
		case 2 -> new MacroMouseAction(basePoint, 315, 591);//2번부대선택
		case 3 -> new MacroMouseAction(basePoint, 343, 591);//3번부대선택
		case 4 -> new MacroMouseAction(basePoint, 374, 591);//4번부대선택
		case 5 -> new MacroMouseAction(basePoint, 397, 591);//5번부대선택
		case 6 -> new MacroMouseAction(basePoint, 424, 591);//6번부대선택
		case 7 -> new MacroMouseAction(basePoint, 453, 591);//7번부대선택
		case 8 -> new MacroMouseAction(basePoint, 485, 591);//8번부대선택
		default -> throw new IllegalArgumentException("Unexpected value: " + number);
		};
	}
	private static MacroAction 부대번호선택(MacroStatus status, Point basePoint) {
		return 부대번호선택(status, basePoint, status.getDarkforceMarchNumber());
	}
	private static MacroAction 상단창좌측공격버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 184, 260);
	}
	private static MacroAction 상단창우측공격버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 319, 260);
	}
	private static MacroAction 하단창좌측공격버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 184, 564);
	}
	private static MacroAction 하단창우측공격버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 319, 564);
	}
	private static MacroAction 출정버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 246, 288);
	}
	private static MacroAction 체력충전창열기(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 104, 19);//체력충전 창열기
	}
	private static MacroAction 체력물약선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 155, 378);//물약선택
	}
	private static MacroAction 체력물약사용(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 257, 486);//물약사용
	}
	private static MacroAction 체력충전창닫기(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 412, 219);//체력충전 창닫기
	}
	private static MacroAction 빈공간선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 250, 170);
	}
	private static MacroAction 적군검색버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 87, 665);
	}
	private static MacroAction 랜덤암흑유닛선택(MacroStatus status, Point basePoint) {
		return 랜덤암흑유닛선택(status, basePoint, 4, 8);
	}
	private static MacroAction 랜덤암흑유닛선택(MacroStatus status, Point basePoint, int begin, int end) {
		int range = end - begin + 1;
		int level = (int)(Math.random() * range) + begin;
		return 암흑레벨선택(status, basePoint, level);
	}
	private static MacroAction 랜덤워해머선택(MacroStatus status, Point basePoint) {
		int level = (int)(Math.random() * 4) + 5;//5,6,7,8
		return 워해머레벨선택(status, basePoint, level);
	}
	private static MacroAction 적군탭선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 109, 317);
	}
	private static MacroAction 집결탭선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 300, 317);
	}
	private static MacroAction 암흑오딘유닛선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 131, 437);//암흑 오딘
	}
	private static MacroAction 테러선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 257, 437);
	}
	private static MacroAction 암흑레벨선택(MacroStatus status, Point basePoint, int level) {
		return switch(level) {
		case 1 -> new MacroMouseAction(basePoint, 170, 597);
		case 2 -> new MacroMouseAction(basePoint, 186, 597);
		case 3 -> new MacroMouseAction(basePoint, 203, 597);
		case 4 -> new MacroMouseAction(basePoint, 218, 597);
		case 5 -> new MacroMouseAction(basePoint, 233, 597);
		case 6 -> new MacroMouseAction(basePoint, 250, 597);
		case 7 -> new MacroMouseAction(basePoint, 266, 597);
		case 8 -> new MacroMouseAction(basePoint, 281, 597);
		case 9 -> new MacroMouseAction(basePoint, 298, 597);
		case 10 -> new MacroMouseAction(basePoint, 312, 597);
		case 11 -> new MacroMouseAction(basePoint, 330, 597);
		default -> throw new IllegalArgumentException("Unexpected value: " + level);
		};
	}
	private static MacroAction 워해머레벨선택(MacroStatus status, Point basePoint, int level) {
		return switch(level) {
		case 1 -> new MacroMouseAction(basePoint, 170, 597);
		case 2 -> new MacroMouseAction(basePoint, 193, 597);
		case 3 -> new MacroMouseAction(basePoint, 216, 597);
		case 4 -> new MacroMouseAction(basePoint, 239, 597);
		case 5 -> new MacroMouseAction(basePoint, 262, 597);
		case 6 -> new MacroMouseAction(basePoint, 285, 597);
		case 7 -> new MacroMouseAction(basePoint, 308, 597);
		case 8 -> new MacroMouseAction(basePoint, 330, 597);
		default -> throw new IllegalArgumentException("Unexpected value: " + level);
		};
	}
	private static MacroAction 테러레벨선택(MacroStatus status, Point basePoint) {
		return switch(status.getTerror4kLevel()) {
		case 1 -> new MacroMouseAction(basePoint, 170, 597);
		case 2 -> new MacroMouseAction(basePoint, 210, 597);
		case 3 -> new MacroMouseAction(basePoint, 250, 597);
		case 4 -> new MacroMouseAction(basePoint, 290, 597);
		case 5 -> new MacroMouseAction(basePoint, 330, 597);
		default -> throw new IllegalArgumentException("Unexpected value: " + status.getTerror4kLevel());
		};
	}
	private static MacroAction 적군검색(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 249, 652);
	}
	private static MacroAction randomDelay(MacroStatus status, double begin, double end) {
		double range = end - begin;
		double delay = Math.random() * range + begin;
		return new MacroDelayAction(delay / status.getScreenList().size());
	}
	private static MacroAction 적선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 246, 353);
	}
	private static MacroAction VIP버튼선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 45, 70);
	}
	private static MacroAction 일일VIP보상클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 390, 120);
	}
	private static MacroAction VIP상점버튼클릭(MacroStatus stauts, Point basePoint) {
		return new MacroMouseAction(basePoint, 140, 180);
	}
	private static MacroAction 장바구니탭선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 165, 75);
	}
	private static MacroAction 장바구니빠른교환클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 387, 662);
	}
	private static MacroAction 뒤로가기(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 83, 23);
	}
	private static MacroAction 패키지상점(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 445, 30);
	}
	private static MacroAction 특별패키지탭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 223, 70);
	}
	private static MacroAction 특별패키지무료보상클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 410, 150);
	}
	private static MacroAction 주간카드탭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 327, 66);
	}
	private static MacroAction 주간카드무료다이아수령(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 377, 166);
	}
	private static MacroAction 일일임무버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 77, 580);
	}
	private static MacroAction 사판훈련(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 353, 600);
	}
	private static MacroAction 사판훈련도전(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 248, 663);
	}
	private static MacroAction 전투스킵(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 25, 116);
	}
	private static MacroAction 사판훈련재도전(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 187, 615);
	}
	private static MacroAction 영웅메뉴(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 466, 408);
	}
	private static MacroAction 영웅모집(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 381, 660);
	}
	private static MacroAction 일반모집(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 85, 645);
	}
	private static MacroAction 스킬모집(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 410, 645);
	}
	private static MacroAction 모집1회(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 159, 532);
	}
	private static MacroAction 모집10회(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 341, 533);
	}
	private static MacroAction 장바구니확인버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 337, 463);
	}
	private static MacroAction 미지의작전이동(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 353, 166);
	}
	private static MacroAction 미지의작전보상버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 95, 143);
	}
	private static MacroAction 미지의작전보상수령(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 249, 568);
	}
	private static MacroAction 원정탐험으로휠이동(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 359, 626, MacroMouseActionType.WHEELDOWN, 15);
	}
	private static MacroAction 원정탐험선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 359, 626);
	}
	private static MacroAction 원정탐험보상클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 175, 573);
	}
	private static MacroAction 원정탐험보상수령(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 252, 621);
	}
	private static MacroAction 원정탐험빠른전투클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 382, 654);
	}
	private static MacroAction 원정탐험빠른전투시작(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 249, 490);
	}
	private static MacroAction 원정탐험빠른전투스킵(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 415, 603);
	}
	private static MacroAction 섬대작전으로이동(MacroStatus status ,Point basePoint) {
		return new MacroMouseAction(basePoint, 349, 634, MacroMouseActionType.WHEELDOWN, 200);
	}
	private static MacroAction 섬대작전선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 349, 634);
	}
	private static MacroAction 섬대작전리셋(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 338, 402);
	}
	private static MacroAction 섬대작전소탕(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 80, 640);
	}	
	private static MacroAction 섬대작전소탕시작(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 247, 571);
	}
	private static MacroAction 섬대작전소탕완료(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 248, 584);
	}
	private static MacroAction 섬대작전나가기(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 244, 404);
	}
}





