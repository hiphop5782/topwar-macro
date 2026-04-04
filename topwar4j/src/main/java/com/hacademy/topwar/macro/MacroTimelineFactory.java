package com.hacademy.topwar.macro;

import java.awt.Point;

import com.hacademy.topwar.macro.action.MacroAction;
import com.hacademy.topwar.macro.action.MacroDelayAction;
import com.hacademy.topwar.macro.action.MacroMouseAction;
import com.hacademy.topwar.macro.action.MacroMouseActionType;
import com.hacademy.topwar.ui.ScreenMode;

public class MacroTimelineFactory {
	
	public static final int singleCount = 2;
	
	public static MacroTimeline 암흑매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(돋보기버튼클릭(status, basePoint));//적군 검색(+다른창닫기)
		timeline.add(돋보기버튼클릭(status, basePoint));//적군 검색
		timeline.add(적군탭선택(status, basePoint));//적군
		
		//무시 설정일 경우는 암흑 종류나 레벨을 선택하지 않도록 변경
		if(status.getDarkforceLevel().equals("무시") == false) {
			timeline.add(암흑오딘유닛선택(status, basePoint));//암흑 오딘
			
			//레벨선택은 랜덤하게 설정(98~102)
			if(status.getDarkforceLevel() == null || status.getDarkforceLevel().equals("랜덤")) {
				timeline.add(랜덤암흑유닛선택(status, basePoint));//레벨선택
			}
			else {
				timeline.add(암흑레벨선택(status, basePoint, 3));
				for(int i=0; i < 3; i++) {
					timeline.add(레벨빼기버튼클릭(status, basePoint));
				}
				int level = Integer.parseInt(status.getDarkforceLevel());
				for(int i=1; i < level; i++) {
					timeline.add(레벨더하기버튼클릭(status, basePoint));
				}
			}
		}
		
		timeline.add(적군검색(status, basePoint));//검색

		//매크로 개수에 맞게 대기(4~6초)
		timeline.add(randomDelay(status, 4, 6));
		
		//공격
		timeline.add(적선택(status, basePoint));//유닛선택
		timeline.add(randomDelay(status, 2, 2.5));
		
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
		timeline.add(randomDelay(status, 2, 2.5));
		
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
	
	public static MacroTimeline 워해머매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(돋보기버튼클릭(status, basePoint));//적군 검색(+다른창닫기)
		timeline.add(돋보기버튼클릭(status, basePoint));//적군 검색
		timeline.add(집결탭선택(status, basePoint));//집결
		
		if(status.getWarhammerType().equals("하트팡팡")) {
			timeline.add(하트팡팡선택(status, basePoint));//하트팡팡
		}
		else {
			timeline.add(워해머선택(status, basePoint));//워해머-4K
		}
		
		//레벨선택
		timeline.add(랜덤워해머선택(status, basePoint));
		
		timeline.add(적군검색(status, basePoint));//검색

		//매크로 개수에 맞게 대기(4~6초)
		timeline.add(randomDelay(status, 4, 6));
		
		//공격
		timeline.add(적선택(status, basePoint));//유닛선택
		timeline.add(randomDelay(status, 2, 2.5));
		
		timeline.add(상단창우측공격버튼(status, basePoint));//일반집결
		
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
		timeline.add(randomDelay(status, 2, 2.5));
		timeline.add(상단창우측공격버튼(status, basePoint));//일반집결
		timeline.add(randomDelay(status, 2, 2.5));
		
		//부대선택
		timeline.add(부대번호선택(status, basePoint));
		
		//출정
		timeline.add(출정버튼클릭(status, basePoint));
		
		//대기 없음
		return timeline;
	}
	
	public static MacroTimeline 워해머반복매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = 워해머매크로(status, basePoint);
		
		//대기 90~100초
		timeline.add(randomDelay(status, 90, 100));
		
		return timeline;
	}
	
	public static MacroTimeline 테러매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(돋보기버튼클릭(status, basePoint));//적군 검색(+다른창닫기)
		timeline.add(돋보기버튼클릭(status, basePoint));//적군 검색
		timeline.add(집결탭선택(status, basePoint));//집결
		timeline.add(테러선택(status, basePoint));//테러-4K
		
		//레벨선택
		timeline.add(테러레벨선택(status, basePoint));
		
		timeline.add(적군검색(status, basePoint));//검색

		//매크로 개수에 맞게 대기(4~6초)
		timeline.add(randomDelay(status, 4, 6));
		
		//공격
		timeline.add(적선택(status, basePoint));//유닛선택
		timeline.add(randomDelay(status, 2, 2.5));
		
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
		timeline.add(randomDelay(status, 2, 2.5));
		//부대선택
		timeline.add(부대번호선택(status, basePoint));
		
		//출정
		timeline.add(출정버튼클릭(status, basePoint));
		
		//대기 없음
		return timeline;
	}
	
	public static MacroTimeline 테러반복매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = 테러매크로(status, basePoint);
		
		//대기 90~100초(자동), 대기 9~10초(수동)
		if(status.isTerror4kManual()) {
			timeline.add(randomDelay(status, 9, 10));
		}
		else {
			timeline.add(randomDelay(status, 90, 100));
		}
		
		return timeline;
	}
	
	public static MacroTimeline 필수퀘스트매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		for(int i=1; i <= 3; i++) {
			//퀘스트받기
			timeline.add(레이더버튼(status, basePoint));
			timeline.add(randomDelay(status, 3, 5));
			timeline.add(필수퀘스트지점(status, basePoint, 1));
			timeline.add(필수퀘스트지점(status, basePoint, 2));
			timeline.add(필수퀘스트지점(status, basePoint, 3));
			timeline.add(필수퀘스트지점(status, basePoint, 4));
			timeline.add(필수퀘스트이동버튼(status, basePoint));
			
			//구조하기(하단중앙) or 암흑 잔재(상단우측) or 암흑 군단 보루(하단우측)
			switch(i) {
			case 1: //구조하기
				timeline.add(필수퀘스트구조하기버튼(status, basePoint)); 
				if(status.isPotion()) {
					timeline.add(체력물약선택(status, basePoint));
					timeline.add(체력물약사용(status, basePoint));
					timeline.add(체력충전창닫기(status, basePoint));
				}
				else {
					timeline.add(빈공간선택(status, basePoint));
				}
				timeline.add(적선택(status, basePoint));
				timeline.add(필수퀘스트구조하기버튼(status, basePoint));
				break;
			case 2://암흑잔재
				timeline.add(암흑잔재공격버튼(status, basePoint));
				if(status.isPotion()) {
					timeline.add(체력물약선택(status, basePoint));
					timeline.add(체력물약사용(status, basePoint));
					timeline.add(체력충전창닫기(status, basePoint));
				}
				else {
					timeline.add(빈공간선택(status, basePoint));
				}
				timeline.add(적선택(status, basePoint));
				timeline.add(암흑잔재공격버튼(status, basePoint));
				timeline.add(부대번호선택(status, basePoint));
				timeline.add(출정버튼클릭(status, basePoint));
				break;
			case 3:
				timeline.add(적선택(status, basePoint));
				timeline.add(하단창우측공격버튼(status, basePoint));
				if(status.isPotion()) {
					timeline.add(체력물약선택(status, basePoint));
					timeline.add(체력물약사용(status, basePoint));
					timeline.add(체력충전창닫기(status, basePoint));
				}
				else {
					timeline.add(빈공간선택(status, basePoint));
				}
				timeline.add(적선택(status, basePoint));
				timeline.add(하단창우측공격버튼(status, basePoint));
				timeline.add(부대번호선택(status, basePoint));
				timeline.add(출정버튼클릭(status, basePoint));
				break;
			}
			
			//완료시까지 대기
			timeline.add(randomDelay(status, 20, 25));
			
			//보상수령
			timeline.add(레이더버튼(status, basePoint));
			timeline.add(randomDelay(status, 3, 5));
			timeline.add(필수퀘스트지점(status, basePoint, 1));
			timeline.add(필수퀘스트지점(status, basePoint, 2));
			timeline.add(필수퀘스트지점(status, basePoint, 3));
			timeline.add(필수퀘스트지점(status, basePoint, 4));
			timeline.add(randomDelay(status, 1, 2));
			timeline.add(레이더글자클릭(status, basePoint));
			timeline.add(레이더글자클릭(status, basePoint));
			
			timeline.add(뒤로가기(status, basePoint));
		}
		
		return timeline;
	}
	public static MacroTimeline 제국의유물매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(돋보기버튼클릭(status, basePoint));//적군 검색(+다른창닫기)
		timeline.add(돋보기버튼클릭(status, basePoint));//적군 검색
		timeline.add(제국의유물탭선택(status, basePoint));
		timeline.add(제국의유물랜덤군종선택(status, basePoint));
		timeline.add(제국의유물스크롤바우측선택(status, basePoint));
		timeline.add(제국의유물레벨더하기버튼선택(status, basePoint));
		timeline.add(제국의유물검색버튼선택(status, basePoint));
		
		//매크로 개수에 맞게 대기(4~6초)
		timeline.add(randomDelay(status, 4, 6));
		
		//공격
		timeline.add(적선택(status, basePoint));//유닛선택
		timeline.add(randomDelay(status, 2, 2.5));
		timeline.add(제국의유물공격버튼(status, basePoint));//타입이 두가지임(버튼1개or2개)
		
		timeline.add(randomDelay(status, 4, 6));
		//부대선택
		timeline.add(부대번호선택(status, basePoint));
		//출정
		timeline.add(출정버튼클릭(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 트럭운송매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(일일임무버튼(status, basePoint));
		timeline.add(randomDelay(status, 3, 5));
		timeline.add(화물차쟁탈전(status, basePoint));
		
		//화물차선택 x2 (기존 수령내역 취소)
		for(int i=1; i <= 5; i++) {
			timeline.add(화물차선택(status, basePoint, i), 1);
			timeline.add(화물차선택(status, basePoint, i), 1);
		}
		
		//화물차운송
		for(int i=1; i <= 5; i++) {
			timeline.add(화물차선택(status, basePoint, i), 1);
			timeline.add(화물차운송버튼(status, basePoint), 1);
			timeline.add(화물차운송안내닫기(status, basePoint), 1);
			timeline.add(화물차갱신버튼(status, basePoint), 1);
			timeline.add(화물차운송버튼(status, basePoint), 1);
			timeline.add(부대번호선택(status, basePoint, i), 1);
			timeline.add(빠른출전버튼클릭(status, basePoint), 1);
			timeline.add(출정버튼클릭(status, basePoint), 1);
		}
		
		//뒤로가기
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 보물1회매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(일일임무버튼(status, basePoint));
		timeline.add(randomDelay(status, 3, 5));
		
		timeline.add(맨아래로이동(status, basePoint));
		timeline.add(보물연맹이동(status, basePoint));
		timeline.add(보물합성(status, basePoint));
		
		//1. 보물 상점인 경우 처리
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));

		//2. 장비 수리인 경우 처리
		for(int i=0; i < 5; i++) {
			timeline.add(보물장비수리게이지우측(status, basePoint));
			timeline.add(보물장비더하기버튼(status, basePoint));
			timeline.add(보물장비더하기버튼(status, basePoint));
			timeline.add(보물장비수리기부버튼(status, basePoint));
		}
		
		//3. 장비 수리 마무리 + 보물 룰렛 돌리기
		timeline.add(장비수리보상수령및룰렛돌리기(status, basePoint));
		timeline.add(randomDelay(status, 0.5, 1));
		
		//4. 고급 수위병 소환
		timeline.add(보물고급수위병소환버튼(status, basePoint));
		
		//4. x버튼 눌러 나가기
		timeline.add(보물x버튼누르기(status, basePoint));
		timeline.add(보물x버튼누르기(status, basePoint));
		timeline.add(보물x버튼누르기(status, basePoint));
		
		//나가기
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));

		timeline.add(randomDelay(status, 2, 3));

		return timeline;
	}
	private static MacroAction 보물고급수위병소환버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 369, 588);
	}
	private static MacroAction 보물x버튼누르기(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 414, 81);
	}
	private static MacroAction 장비수리보상수령및룰렛돌리기(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 251, 613);
	}
	private static MacroAction 보물장비더하기버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 318, 385);
	}
	private static MacroAction 보물장비수리기부버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 250, 441);
	}
	private static MacroAction 보물장비수리게이지우측(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 289, 387);
	}
	private static MacroAction 보물합성(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 250, 630);
	}
	
	public static MacroTimeline 사판훈련매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(일일임무버튼(status, basePoint));
		timeline.add(randomDelay(status, 3, 5));
		timeline.add(사판훈련으로이동(status, basePoint));
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
		timeline.add(주간카드탭으로이동(status, basePoint));
		timeline.add(randomDelay(status, 1, 2));
		timeline.add(주간카드탭(status, basePoint));
		timeline.add(보석함클릭(status, basePoint));
		
		for(int i=0; i < 20; i++) {
			timeline.add(주간카드무료다이아수령(status, basePoint));
			timeline.add(randomDelay(status, 16, 18));
		}
		
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 고급모집매크로(MacroStatus status, Point basePoint, int count) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(영웅메뉴(status, basePoint));
		timeline.add(영웅모집(status, basePoint));
		timeline.add(randomDelay(status, 1, 2));
		
		for(int i=0; i < count; i++) {
			//3회 누르거나 딜레이를 줘야 다시 원래화면으로 옴
			timeline.add(모집1회(status, basePoint));
			//timeline.add(모집1회(status, basePoint));//다중화면에서 부적합
			timeline.add(randomDelay(status, 5, 6));//다중화면에서 적합
			timeline.add(모집1회(status, basePoint));
			timeline.add(randomDelay(status, 1, 2));
		}
		
		timeline.add(randomDelay(status, 1, 2));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 일반모집스킬모집매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(영웅메뉴(status, basePoint));
		timeline.add(영웅모집(status, basePoint));
		timeline.add(randomDelay(status, 1, 2));
		
		timeline.add(일반모집탭선택(status, basePoint));
		timeline.add(모집1회(status, basePoint));
		timeline.add(randomDelay(status, 5, 6));//다중화면에서 적합
		timeline.add(모집1회(status, basePoint));
		
		timeline.add(스킬모집탭선택(status, basePoint));
		timeline.add(모집1회(status, basePoint));
		timeline.add(randomDelay(status, 5, 6));//다중화면에서 적합
		timeline.add(모집1회(status, basePoint));
		
		timeline.add(randomDelay(status, 1, 2));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	//스크린이 1개일 때 최소딜레이 - 클릭당 0.5초
	public static MacroTimeline VIP보상받기매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(VIP버튼선택(status, basePoint));		
		timeline.add(일일VIP보상클릭(status, basePoint));	
		timeline.add(일일VIP보상클릭(status, basePoint));	
		
		timeline.add(뒤로가기(status, basePoint));		
		
		return timeline;
	}
	public static MacroTimeline 장바구니매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(VIP버튼선택(status, basePoint));		
		timeline.add(VIP상점버튼클릭(status, basePoint));	
		timeline.add(처음탭으로이동(status, basePoint));		
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
	public static MacroTimeline 패키지무료보상매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(패키지상점(status, basePoint));			
		timeline.add(특별패키지탭(status, basePoint));			
		timeline.add(특별패키지무료보상클릭(status, basePoint));	
		timeline.add(뒤로가기(status, basePoint));				
		timeline.add(뒤로가기(status, basePoint));				
				
		return timeline;
	}
	public static MacroTimeline 주간장식세트무료쿠폰매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(VIP버튼선택(status, basePoint));			
		timeline.add(VIP상점버튼클릭(status, basePoint));		
		timeline.add(마지막탭으로이동(status, basePoint));		
		timeline.add(장식세트상점탭클릭(status, basePoint));		
		timeline.add(장식세트토큰받기버튼클릭(status, basePoint));	
		timeline.add(장식세트무료토큰받기클릭(status, basePoint));	
		timeline.add(장식세트무료토큰받기클릭(status, basePoint));	
		timeline.add(뒤로가기(status, basePoint));				
		timeline.add(뒤로가기(status, basePoint));				
		timeline.add(뒤로가기(status, basePoint));		
		
		return timeline;
	}
	public static MacroTimeline 석유시설매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(돋보기버튼클릭(status, basePoint));			
		timeline.add(돋보기버튼클릭(status, basePoint));			
		timeline.add(자원지탭선택(status, basePoint));			
		timeline.add(randomDelay(status, 2, 3));			
		timeline.add(자원지마지막탭으로이동(status, basePoint));	
		timeline.add(randomDelay(status, 2, 3));			
		timeline.add(유전선택(status, basePoint));				
		
		timeline.add(최소레벨선택(status, basePoint));			
		for(int i=2; i < status.getOilFacilityLevel(); i++) {
			timeline.add(레벨더하기버튼클릭(status, basePoint));	
		}
		timeline.add(자원지검색버튼(status, basePoint));			
		timeline.add(randomDelay(status, 3, 5));			
		
		timeline.add(화면중앙클릭(status, basePoint));			
		timeline.add(채집시설버튼클릭(status, basePoint));		
		timeline.add(빠른출전버튼클릭(status, basePoint));	
//		timeline.add(슬롯4번선택(status, basePoint));
//		timeline.add(슬롯5번선택(status, basePoint));
//		timeline.add(슬롯6번선택(status, basePoint));
		timeline.add(슬롯7번선택(status, basePoint));
		timeline.add(슬롯8번선택(status, basePoint));
		timeline.add(슬롯9번선택(status, basePoint));
		timeline.add(출정버튼클릭(status, basePoint));
		timeline.add(자원건설확인버튼클릭(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 식량시설매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(돋보기버튼클릭(status, basePoint));
		timeline.add(돋보기버튼클릭(status, basePoint));
		timeline.add(자원지탭선택(status, basePoint));
		timeline.add(randomDelay(status, 2, 3));
		timeline.add(자원지마지막탭으로이동(status, basePoint));
		timeline.add(randomDelay(status, 2, 3));
		timeline.add(농지선택(status, basePoint));
		timeline.add(최소레벨선택(status, basePoint));
		for(int i=2; i < status.getFoodFacilityLevel(); i++) {
			timeline.add(레벨더하기버튼클릭(status, basePoint));
		}
		timeline.add(자원지검색버튼(status, basePoint));
		timeline.add(randomDelay(status, 3, 5));
		
		timeline.add(화면중앙클릭(status, basePoint));
		timeline.add(채집시설버튼클릭(status, basePoint));
		timeline.add(빠른출전버튼클릭(status, basePoint));
//		timeline.add(슬롯4번선택(status, basePoint));
//		timeline.add(슬롯5번선택(status, basePoint));
//		timeline.add(슬롯6번선택(status, basePoint));
		timeline.add(슬롯7번선택(status, basePoint));
		timeline.add(슬롯8번선택(status, basePoint));
		timeline.add(슬롯9번선택(status, basePoint));
		timeline.add(출정버튼클릭(status, basePoint));
		timeline.add(자원건설확인버튼클릭(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 오딘시설매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(돋보기버튼클릭(status, basePoint));
		timeline.add(돋보기버튼클릭(status, basePoint));
		timeline.add(자원지탭선택(status, basePoint));
		timeline.add(randomDelay(status, 2, 3));
		timeline.add(자원지마지막탭으로이동(status, basePoint));
		timeline.add(randomDelay(status, 2, 3));
		timeline.add(오딘광맥선택(status, basePoint));
		timeline.add(최소레벨선택(status, basePoint));
		for(int i=1; i < status.getOdinFacilityLevel(); i++) {
			timeline.add(레벨더하기버튼클릭(status, basePoint));
		}
		timeline.add(자원지검색버튼(status, basePoint));
		timeline.add(randomDelay(status, 3, 5));
		
		timeline.add(화면중앙클릭(status, basePoint));
		timeline.add(채집시설버튼클릭(status, basePoint));
		timeline.add(빠른출전버튼클릭(status, basePoint));
		timeline.add(출정버튼클릭(status, basePoint));
		timeline.add(자원건설확인버튼클릭(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 일일임무매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(일일임무버튼(status, basePoint));
		timeline.add(일일임무일괄수령버튼클릭(status, basePoint));
		timeline.add(일일임무일괄수령진행버튼클릭(status, basePoint));
		timeline.add(일일임무일괄수령확인버튼클릭(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 크로스패배매크로(MacroStatus status, Point basePoint, int count) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(일일임무버튼(status, basePoint));
//		timeline.add(섬대작전으로이동(status, basePoint));
		timeline.add(크로스훈련으로절반이동(status, basePoint));
		timeline.add(크로스훈련으로절반이동(status, basePoint));
		timeline.add(randomDelay(status, 2, 3));
//		timeline.add(크로스훈련선택(status, basePoint));
		timeline.add(크로스훈련적선택(status, basePoint));
		timeline.add(크로스훈련도전버튼(status, basePoint));
		
		for(int i=0; i < count; i++) {
			timeline.add(크로스훈련적선택(status, basePoint));
			
			if(i == 0) {
				timeline.add(슬롯1번선택(status, basePoint));
				timeline.add(슬롯2번선택(status, basePoint));
				timeline.add(슬롯3번선택(status, basePoint));
				timeline.add(슬롯4번선택(status, basePoint));
				timeline.add(슬롯5번선택(status, basePoint));
				timeline.add(슬롯6번선택(status, basePoint));
				timeline.add(슬롯7번선택(status, basePoint));
				timeline.add(슬롯8번선택(status, basePoint));
				timeline.add(슬롯9번선택(status, basePoint));
				timeline.add(첫번째유닛선택(status, basePoint));
			}
			timeline.add(전투버튼(status, basePoint));
			
			timeline.add(randomDelay(status, 2, 3));
			timeline.add(전투스킵(status, basePoint));
			timeline.add(크로스훈련뒤로버튼(status, basePoint));
		}
		
		timeline.add(크로스훈련구매창X버튼(status, basePoint));
		timeline.add(크로스훈련X버튼(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}	
	public static MacroTimeline 골드지원요청(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(길드메뉴(status, basePoint));
		timeline.add(randomDelay(status, 0.5, 0.7));
		//기존 작업을 취소하는 처리
		timeline.add(길드지원(status, basePoint));
		timeline.add(길드지원요청또는요청취소(status, basePoint));
		timeline.add(길드지원요청취소확인창에서확인버튼(status, basePoint));
		timeline.add(길드지원요청또는요청취소옆빈공간(status, basePoint));//진행된 작업이 없을경우 대비
		
		//골드 지원 요청 처리
		timeline.add(길드지원요청또는요청취소(status, basePoint));
		timeline.add(길드지원골드선택(status, basePoint));
		timeline.add(길드지원재료선택창에서지원요청버튼(status, basePoint));
		timeline.add(randomDelay(status, 0.5, 0.7));
		
		//골드 지원 요청 취소 처리
		timeline.add(길드지원요청또는요청취소(status, basePoint));
		timeline.add(길드지원요청취소확인창에서확인버튼(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 재료지원요청(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(길드메뉴(status, basePoint));
		timeline.add(randomDelay(status, 0.5, 0.7));
		//기존 작업을 취소하는 처리
		timeline.add(길드지원(status, basePoint));
		timeline.add(길드지원요청또는요청취소(status, basePoint));
		timeline.add(길드지원요청취소확인창에서취소버튼(status, basePoint));
		timeline.add(길드지원요청또는요청취소옆빈공간(status, basePoint));//진행된 작업이 없을경우 대비
		timeline.add(randomDelay(status, 0.5, 0.7));
		timeline.add(길드지원모두지원버튼(status, basePoint));
		
		//재료 지원 요청 처리
		timeline.add(길드지원요청또는요청취소(status, basePoint));
		int type = (int)(Math.random() * 4) + 1;//일단 랜덤
		timeline.add(길드지원재료선택(status, basePoint, type));
		timeline.add(길드지원재료선택창에서지원요청버튼(status, basePoint));
		timeline.add(randomDelay(status, 0.5, 0.7));
		timeline.add(길드지원요청취소확인창에서취소버튼(status, basePoint));
		
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 월드기지전환(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(월드기지전환버튼(status, basePoint));
		timeline.add(randomDelay(status, 1, 1.2));
		return timeline;
	}
	public static MacroTimeline 기지외부로이동(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(레이더버튼(status, basePoint));
		timeline.add(randomDelay(status, 1, 1.5));
		timeline.add(월드기지전환버튼(status, basePoint));
		timeline.add(randomDelay(status, 1, 1.5));
		timeline.add(레이더버튼(status, basePoint));
		timeline.add(randomDelay(status, 1, 1.5));
		timeline.add(뒤로가기(status, basePoint));
		return timeline;
	}
	public static MacroTimeline 기지내부로이동(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = 기지외부로이동(status, basePoint);
		timeline.add(월드기지전환버튼(status, basePoint));
		return timeline;
	}
	public static MacroTimeline 길드기부매크로(MacroStatus status, Point basePoint, int count) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(좌측사이드메뉴선택(status, basePoint));
		timeline.add(randomDelay(status, 1.2, 1.5));
		timeline.add(좌측사이드메뉴길드기부버튼(status, basePoint));
		timeline.add(randomDelay(status, 1.2, 1.5));
		
		for(int i=0; i < count; i++) {
			timeline.add(길드과학기술기부버튼클릭(status, basePoint));
		}
		
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 재료생산매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(좌측사이드메뉴선택(status, basePoint));
		timeline.add(randomDelay(status, 1, 1.2));
		timeline.add(좌측사이드메뉴재료생산버튼(status, basePoint));
		timeline.add(randomDelay(status, 1, 1.2));
		
		for(int i=0; i < 6; i++) {
			timeline.add(재료생산클릭(status, basePoint));
			timeline.add(randomDelay(status, 0.2, 0.4));
		}
		
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 괴물기부매크로(MacroStatus status, Point basePoint, int count) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(길드메뉴(status, basePoint));
		timeline.add(randomDelay(status, 1, 1.5));
		timeline.add(맨아래로이동(status, basePoint));
		timeline.add(길드메뉴에서괴물클릭(status, basePoint));
		
		timeline.add(randomDelay(status, 1, 1.5));
		
		timeline.add(길드괴물레벨업버튼(status, basePoint));
		
		for(int i=0; i < count; i++) {
			timeline.add(길드괴물기부버튼(status, basePoint));
		}
		
		timeline.add(길드괴물기부닫기버튼(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	
	public static MacroTimeline 일일매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(기지내부로이동(status, basePoint));
		timeline.add(randomDelay(status, 3, 3.5));
		
		if(status.isDailyVipReward())
			timeline.add(VIP보상받기매크로(status, basePoint));
		if(status.isDailyBasketReward())
			timeline.add(장바구니매크로(status, basePoint));
		if(status.isDailySpecialReward()) 
			timeline.add(패키지무료보상매크로(status, basePoint));
		if(status.isDailyGemReward())
			timeline.add(무료보석수집매크로(status, basePoint));
		if(status.isDailyQuestReward())
			timeline.add(일일임무매크로(status, basePoint));
		if(status.isDailySandTraning())
			timeline.add(사판훈련매크로(status, basePoint));
		if(status.isDailyNormalIncrutAndSkill())
			timeline.add(일반모집스킬모집매크로(status, basePoint));
		if(status.isDailyCrossBattle()) 
			timeline.add(크로스패배매크로(status, basePoint, 10));
		if(status.isDailyAdvancedIncruit())
			timeline.add(고급모집매크로(status, basePoint, 2));
		if(status.isWeeklyDecorFreeToken())
			timeline.add(주간장식세트무료쿠폰매크로(status, basePoint));
		if(status.isOilFacility())
			timeline.add(석유시설매크로(status, basePoint));
		if(status.isFoodFacility())
			timeline.add(식량시설매크로(status, basePoint));
		if(status.isOdinFacility())
			timeline.add(오딘시설매크로(status, basePoint));
		
		return timeline;
	}
	
	//부대선택
	private static MacroAction 부대번호선택(MacroStatus status, Point basePoint, int number) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return switch(number) {
			case 1 -> new MacroMouseAction(basePoint, 370, 365, status.getScreenList().size() < singleCount);//1번부대선택
			case 2 -> new MacroMouseAction(basePoint, 388, 365, status.getScreenList().size() < singleCount);//2번부대선택
			case 3 -> new MacroMouseAction(basePoint, 404, 365, status.getScreenList().size() < singleCount);//3번부대선택
			case 4 -> new MacroMouseAction(basePoint, 422, 365, status.getScreenList().size() < singleCount);//4번부대선택
			case 5 -> new MacroMouseAction(basePoint, 440, 365, status.getScreenList().size() < singleCount);//5번부대선택
			case 6 -> new MacroMouseAction(basePoint, 458, 365, status.getScreenList().size() < singleCount);//6번부대선택
			case 7 -> new MacroMouseAction(basePoint, 476, 365, status.getScreenList().size() < singleCount);//7번부대선택
			case 8 -> new MacroMouseAction(basePoint, 492, 365, status.getScreenList().size() < singleCount);//8번부대선택
			default -> throw new IllegalArgumentException("Unexpected value: " + number);
			};
		}
		
		return switch(number) {
		case 1 -> new MacroMouseAction(basePoint, 286, 591, status.getScreenList().size() < singleCount);//1번부대선택
		case 2 -> new MacroMouseAction(basePoint, 315, 591, status.getScreenList().size() < singleCount);//2번부대선택
		case 3 -> new MacroMouseAction(basePoint, 343, 591, status.getScreenList().size() < singleCount);//3번부대선택
		case 4 -> new MacroMouseAction(basePoint, 374, 591, status.getScreenList().size() < singleCount);//4번부대선택
		case 5 -> new MacroMouseAction(basePoint, 397, 591, status.getScreenList().size() < singleCount);//5번부대선택
		case 6 -> new MacroMouseAction(basePoint, 424, 591, status.getScreenList().size() < singleCount);//6번부대선택
		case 7 -> new MacroMouseAction(basePoint, 453, 591, status.getScreenList().size() < singleCount);//7번부대선택
		case 8 -> new MacroMouseAction(basePoint, 485, 591, status.getScreenList().size() < singleCount);//8번부대선택
		default -> throw new IllegalArgumentException("Unexpected value: " + number);
		};
	}
	private static MacroAction 부대번호선택(MacroStatus status, Point basePoint) {
		return 부대번호선택(status, basePoint, status.getDarkforceMarchNumber());
	}
	private static MacroAction 상단창좌측공격버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 207, 154, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 184, 260, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 상단창우측공격버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 295, 154, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 319, 260, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 하단창좌측공격버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 207, 347, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 184, 564, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 하단창우측공격버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 295, 347, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 319, 564, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 출정버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 247, 175, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 246, 288, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 체력물약선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 189, 227, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 155, 378, status.getScreenList().size() < singleCount);//물약선택
	}
	private static MacroAction 체력물약사용(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 249, 294, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 257, 486, status.getScreenList().size() < singleCount);//물약사용
	}
	private static MacroAction 체력충전창닫기(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 351, 134, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 412, 219, status.getScreenList().size() < singleCount);//체력충전 창닫기
	}
	private static MacroAction 빈공간선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 250, 105, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 250, 170, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 돋보기버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 155, 410, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 87, 665, status.getScreenList().size() < singleCount);
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
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 163, 192, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 109, 317, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 집결탭선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 282, 192, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 300, 317, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 제국의유물탭선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 341, 192, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 395, 317, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 제국의유물스크롤바우측선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 295, 363, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 320, 597, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 제국의유물레벨더하기버튼선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 314, 361, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 355, 597, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 제국의유물검색버튼선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 253, 401, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 253, 654, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 제국의유물랜덤군종선택(MacroStatus status, Point basePoint) {
		int choice = (int)(Math.random() * 3);
		return switch(choice) {
		case 0->제국의유물육군선택(status, basePoint);
		case 1->제국의유물해군선택(status, basePoint);
		default->제국의유물공군선택(status, basePoint);
		};
	}
	private static MacroAction 제국의유물육군선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 176, 265, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 131, 433, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 제국의유물해군선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 252, 265, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 257, 433, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 제국의유물공군선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 333, 265, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 386, 433, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 암흑오딘유닛선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 176, 268, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 131, 437, status.getScreenList().size() < singleCount);//암흑 오딘
	}
	private static MacroAction 워해머선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 176, 268, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 134, 437, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 테러선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 255, 267, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 257, 437, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 하트팡팡선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 319, 268, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 383, 437, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 암흑레벨선택(MacroStatus status, Point basePoint, int level) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return switch(level) {
			case 1 -> new MacroMouseAction(basePoint, 199, 368, status.getScreenList().size() < singleCount);
			case 2 -> new MacroMouseAction(basePoint, 210, 368, status.getScreenList().size() < singleCount);
			case 3 -> new MacroMouseAction(basePoint, 219, 368, status.getScreenList().size() < singleCount);
			case 4 -> new MacroMouseAction(basePoint, 230, 368, status.getScreenList().size() < singleCount);
			case 5 -> new MacroMouseAction(basePoint, 239, 368, status.getScreenList().size() < singleCount);
			case 6 -> new MacroMouseAction(basePoint, 250, 368, status.getScreenList().size() < singleCount);
			case 7 -> new MacroMouseAction(basePoint, 259, 368, status.getScreenList().size() < singleCount);
			case 8 -> new MacroMouseAction(basePoint, 270, 368, status.getScreenList().size() < singleCount);
			case 9 -> new MacroMouseAction(basePoint, 279, 368, status.getScreenList().size() < singleCount);
			case 10 -> new MacroMouseAction(basePoint, 290, 368, status.getScreenList().size() < singleCount);
			case 11 -> new MacroMouseAction(basePoint, 300, 368, status.getScreenList().size() < singleCount);
			default -> throw new IllegalArgumentException("Unexpected value: " + level);
			};
		}
		return switch(level) {
		case 1 -> new MacroMouseAction(basePoint, 170, 597, status.getScreenList().size() < singleCount);
		case 2 -> new MacroMouseAction(basePoint, 186, 597, status.getScreenList().size() < singleCount);
		case 3 -> new MacroMouseAction(basePoint, 203, 597, status.getScreenList().size() < singleCount);
		case 4 -> new MacroMouseAction(basePoint, 218, 597, status.getScreenList().size() < singleCount);
		case 5 -> new MacroMouseAction(basePoint, 233, 597, status.getScreenList().size() < singleCount);
		case 6 -> new MacroMouseAction(basePoint, 250, 597, status.getScreenList().size() < singleCount);
		case 7 -> new MacroMouseAction(basePoint, 266, 597, status.getScreenList().size() < singleCount);
		case 8 -> new MacroMouseAction(basePoint, 281, 597, status.getScreenList().size() < singleCount);
		case 9 -> new MacroMouseAction(basePoint, 298, 597, status.getScreenList().size() < singleCount);
		case 10 -> new MacroMouseAction(basePoint, 312, 597, status.getScreenList().size() < singleCount);
		case 11 -> new MacroMouseAction(basePoint, 330, 597, status.getScreenList().size() < singleCount);
		default -> throw new IllegalArgumentException("Unexpected value: " + level);
		};
	}
	private static MacroAction 워해머레벨선택(MacroStatus status, Point basePoint, int level) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return switch(level) {
			case 1 -> new MacroMouseAction(basePoint, 205, 366, status.getScreenList().size() < singleCount);
			case 2 -> new MacroMouseAction(basePoint, 216, 366, status.getScreenList().size() < singleCount);
			case 3 -> new MacroMouseAction(basePoint, 230, 366, status.getScreenList().size() < singleCount);
			case 4 -> new MacroMouseAction(basePoint, 243, 366, status.getScreenList().size() < singleCount);
			case 5 -> new MacroMouseAction(basePoint, 257, 366, status.getScreenList().size() < singleCount);
			case 6 -> new MacroMouseAction(basePoint, 271, 366, status.getScreenList().size() < singleCount);
			case 7 -> new MacroMouseAction(basePoint, 285, 366, status.getScreenList().size() < singleCount);
			case 8 -> new MacroMouseAction(basePoint, 298, 366, status.getScreenList().size() < singleCount);
			default -> throw new IllegalArgumentException("Unexpected value: " + level);
			};
		}
		return switch(level) {
		case 1 -> new MacroMouseAction(basePoint, 170, 597, status.getScreenList().size() < singleCount);
		case 2 -> new MacroMouseAction(basePoint, 193, 597, status.getScreenList().size() < singleCount);
		case 3 -> new MacroMouseAction(basePoint, 216, 597, status.getScreenList().size() < singleCount);
		case 4 -> new MacroMouseAction(basePoint, 239, 597, status.getScreenList().size() < singleCount);
		case 5 -> new MacroMouseAction(basePoint, 262, 597, status.getScreenList().size() < singleCount);
		case 6 -> new MacroMouseAction(basePoint, 285, 597, status.getScreenList().size() < singleCount);
		case 7 -> new MacroMouseAction(basePoint, 308, 597, status.getScreenList().size() < singleCount);
		case 8 -> new MacroMouseAction(basePoint, 330, 597, status.getScreenList().size() < singleCount);
		default -> throw new IllegalArgumentException("Unexpected value: " + level);
		};
	}
	private static MacroAction 테러레벨선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return switch(status.getTerror4kLevel()) {
			case 1 -> new MacroMouseAction(basePoint, 200, 363, status.getScreenList().size() < singleCount);
			case 2 -> new MacroMouseAction(basePoint, 224, 363, status.getScreenList().size() < singleCount);
			case 3 -> new MacroMouseAction(basePoint, 248, 363, status.getScreenList().size() < singleCount);
			case 4 -> new MacroMouseAction(basePoint, 272, 363, status.getScreenList().size() < singleCount);
			case 5 -> new MacroMouseAction(basePoint, 298, 363, status.getScreenList().size() < singleCount);
			default -> throw new IllegalArgumentException("Unexpected value: " + status.getTerror4kLevel());
			};
		}
		return switch(status.getTerror4kLevel()) {
		case 1 -> new MacroMouseAction(basePoint, 170, 597, status.getScreenList().size() < singleCount);
		case 2 -> new MacroMouseAction(basePoint, 210, 597, status.getScreenList().size() < singleCount);
		case 3 -> new MacroMouseAction(basePoint, 250, 597, status.getScreenList().size() < singleCount);
		case 4 -> new MacroMouseAction(basePoint, 290, 597, status.getScreenList().size() < singleCount);
		case 5 -> new MacroMouseAction(basePoint, 330, 597, status.getScreenList().size() < singleCount);
		default -> throw new IllegalArgumentException("Unexpected value: " + status.getTerror4kLevel());
		};
	}
	private static MacroAction randomDelay(MacroStatus status, double begin, double end) {
		double range = end - begin;
		double delay = Math.random() * range + begin;
		return new MacroDelayAction(delay / status.getScreenList().size());
	}
	private static MacroAction 레이더버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 476, 215, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 460, 352, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 적군검색(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 250, 396, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 249, 652, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 적선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 250, 214, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 246, 353, status.getScreenList().size() < singleCount);
	}
	private static MacroAction VIP버튼선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 29, 42, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 45, 70, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 일일VIP보상클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 338, 69, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 390, 120, status.getScreenList().size() < singleCount);
	}
	private static MacroAction VIP상점버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 181, 113, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 140, 180, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 처음탭으로이동(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 232, 42, MacroMouseActionType.WHEELDOWN, 20, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 225, 75, MacroMouseActionType.WHEELDOWN, 20, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 마지막탭으로이동(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 232, 42, MacroMouseActionType.WHEELUP, 200, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 225, 75, MacroMouseActionType.WHEELUP, 200, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 장바구니탭선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 232, 42, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 225, 75, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 장바구니빠른교환클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 335, 402, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 387, 662, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 뒤로가기(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 145, 19, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 83, 23, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 패키지상점(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 468, 17, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 445, 30, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 특별패키지탭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 234, 44, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 223, 70, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 특별패키지무료보상클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 347, 91, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 410, 150, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 주간카드탭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 345, 41, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 342, 72, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 주간카드탭으로이동(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 330, 45, 247, 45, MacroMouseActionType.DRAG);
		}
		return new MacroMouseAction(basePoint, 342, 72, 189, 72, MacroMouseActionType.DRAG);
	}
	private static MacroAction 보석함클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 338, 91, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 397, 141, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 주간카드무료다이아수령(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 309, 189, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 354, 318, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 일일임무버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 146, 359, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 77, 580, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 일일임무일괄수령버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 313, 81, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 348, 135, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 일일임무일괄수령진행버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 250, 336, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 250, 550, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 일일임무일괄수령확인버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 250, 370, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 250, 615, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 사판훈련으로이동(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 350, 370, 350, 235, MacroMouseActionType.DRAG, status.getScreenList().size() < singleCount);
		}
		//return new MacroMouseAction(basePoint, 354, 640, MacroMouseActionType.WHEELDOWN, 10);
		return new MacroMouseAction(basePoint, 354, 640, 354, 430, MacroMouseActionType.DRAG, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 사판훈련(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 312, 392, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 353, 640, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 사판훈련도전(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 213, 403, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 200, 663, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 전투스킵(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 14, 68, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 25, 116, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 사판훈련재도전(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 210, 377, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 187, 615, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 영웅메뉴(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 475, 250, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 466, 408, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 영웅모집(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 329, 406, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 381, 660, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 일반모집(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 150, 397, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 85, 645, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 스킬모집(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 350, 397, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 410, 645, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 모집1회(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 193, 327, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 159, 532, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 모집10회(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 305, 328, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 341, 533, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 장바구니확인버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 304, 282, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 337, 463, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 미지의작전이동(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 311, 174, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 353, 166, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 미지의작전보상버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 154, 84, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 95, 143, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 미지의작전보상수령(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 249, 342, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 249, 568, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 장식세트상점탭클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 145, 40, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 74, 70, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 장식세트토큰받기버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 309, 405, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 352, 663, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 장식세트무료토큰받기클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 193, 75, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 162, 137, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 자원지탭선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 222, 194, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 205, 319, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 유전선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 169, 265, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 123, 435, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 농지선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 252, 265, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 247, 435, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 오딘광맥선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 331, 265, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 383, 435, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 레벨더하기버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 315, 360, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 354, 595, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 레벨빼기버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 185, 360, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 146, 595, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 자원지검색버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 250, 395, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 244, 644, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 화면중앙클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 250, 212, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 246, 345, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 채집시설버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 211, 164, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 189, 267, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 빠른출전버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 483, 341, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 467, 548, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 자원건설확인버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 250, 279, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 248, 456, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 자원지마지막탭으로이동(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 252, 265, MacroMouseActionType.WHEELUP, 100, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 247, 435, MacroMouseActionType.WHEELUP, 100, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 일반모집탭선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 150, 395, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 80, 645, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 스킬모집탭선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 350, 395, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 414, 645, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 크로스훈련구매창X버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 347, 117, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 417, 193, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 크로스훈련X버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 348, 68, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 409, 111, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 크로스훈련뒤로버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			
		}
		return new MacroMouseAction(basePoint, 251, 604, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 전투버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			
		}
		return new MacroMouseAction(basePoint, 244, 285, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 첫번째유닛선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 24, 397, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 29, 643, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 슬롯9번선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 293, 331, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 318, 538, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 슬롯8번선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 222, 318, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 202, 519, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 슬롯7번선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 156, 302, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 89, 497, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 슬롯6번선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 324, 291, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 363, 481, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 슬롯5번선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 252, 279, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 249, 458, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 슬롯4번선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 175, 269, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 129, 439, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 슬롯3번선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 345, 258, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 404, 420, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 슬롯2번선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 271, 245, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 289, 400, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 슬롯1번선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			
		}
		return new MacroMouseAction(basePoint, 181, 376, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 크로스훈련적선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			
		}
		return new MacroMouseAction(basePoint, 365, 550, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 크로스훈련도전버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 205, 230, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 251, 653, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 크로스훈련으로절반이동(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 351, 386, 351, 208, MacroMouseActionType.DRAG);
		}
		//return new MacroMouseAction(basePoint, 351, 623, MacroMouseActionType.WHEELUP, 20);
		return new MacroMouseAction(basePoint, 351, 660, 351, 370, MacroMouseActionType.DRAG);
	}
	private static MacroAction 길드메뉴(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 475, 291, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 471, 469, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 길드지원(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 309, 245, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 348, 398, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 길드지원요청또는요청취소(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 319, 370, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 366, 607, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 길드지원요청또는요청취소옆빈공간(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 270, 370, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 241, 607, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 길드지원요청취소확인창에서취소버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 203, 277, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 174, 462, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 길드지원요청취소확인창에서확인버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 298, 280, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 328, 462, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 길드지원재료선택(MacroStatus status, Point basePoint, int type) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return switch(type) {
			case 1 -> new MacroMouseAction(basePoint, 173, 145, status.getScreenList().size() < singleCount);
			case 2 -> new MacroMouseAction(basePoint, 223, 145, status.getScreenList().size() < singleCount);
			case 3 -> new MacroMouseAction(basePoint, 276, 145, status.getScreenList().size() < singleCount);
			case 4 -> new MacroMouseAction(basePoint, 326, 145, status.getScreenList().size() < singleCount);
			default -> null;
			};
		}
		return switch(type) {
		case 1 -> new MacroMouseAction(basePoint, 129, 240, status.getScreenList().size() < singleCount);
		case 2 -> new MacroMouseAction(basePoint, 214, 240, status.getScreenList().size() < singleCount);
		case 3 -> new MacroMouseAction(basePoint, 292, 240, status.getScreenList().size() < singleCount);
		case 4 -> new MacroMouseAction(basePoint, 375, 240, status.getScreenList().size() < singleCount);
		default -> null;
		};
	}
	private static MacroAction 길드지원골드선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 224, 234, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 205, 384, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 길드지원재료선택창에서지원요청버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 250, 307, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 255, 517, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 길드지원모두지원버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 250, 318, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 252, 510, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 최소레벨선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 203, 367, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 175, 594, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 좌측사이드메뉴선택(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 8, 215, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 10, 349, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 좌측사이드메뉴육군훈련버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 99, 140, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 160, 229, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 좌측사이드메뉴해군훈련버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 99, 163, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 160, 267, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 좌측사이드메뉴공군훈련버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 99, 186, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 160, 302, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 좌측사이드메뉴재료생산버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 99, 273, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 160, 441, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 좌측사이드메뉴길드기부버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 99, 292, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 160, 476, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 월드기지전환버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 347, 411, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 409, 665, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 길드과학기술기부버튼클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 305, 317, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 336, 515, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 재료생산클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			switch(status.getProductMaterialType()) {
			case "강철": 		return new MacroMouseAction(basePoint, 170, 321, status.getScreenList().size() < singleCount);
			case "나사":		return new MacroMouseAction(basePoint, 225, 321, status.getScreenList().size() < singleCount);
			case "트랜지스터":	return new MacroMouseAction(basePoint, 275, 321, status.getScreenList().size() < singleCount);
			case "고무":		return new MacroMouseAction(basePoint, 325, 321, status.getScreenList().size() < singleCount);
			case "텅스텐":	return new MacroMouseAction(basePoint, 170, 375, status.getScreenList().size() < singleCount);
			case "배터리":	return new MacroMouseAction(basePoint, 225, 375, status.getScreenList().size() < singleCount);
			case "유리":		return new MacroMouseAction(basePoint, 275, 375, status.getScreenList().size() < singleCount);
			default:		return new MacroMouseAction(basePoint, 170, 321, status.getScreenList().size() < singleCount);
			}
		}
		switch(status.getProductMaterialType()) {
		case "강철": 		return new MacroMouseAction(basePoint, 117, 522, status.getScreenList().size() < singleCount);
		case "나사":		return new MacroMouseAction(basePoint, 206, 522, status.getScreenList().size() < singleCount);
		case "트랜지스터":	return new MacroMouseAction(basePoint, 290, 522, status.getScreenList().size() < singleCount);
		case "고무":		return new MacroMouseAction(basePoint, 379, 522, status.getScreenList().size() < singleCount);
		case "텅스텐":	return new MacroMouseAction(basePoint, 122, 606, status.getScreenList().size() < singleCount);
		case "배터리":	return new MacroMouseAction(basePoint, 203, 606, status.getScreenList().size() < singleCount);
		case "유리":		return new MacroMouseAction(basePoint, 294, 606, status.getScreenList().size() < singleCount);
		default:		return new MacroMouseAction(basePoint, 117, 522, status.getScreenList().size() < singleCount);
		}
	}
	
	private static MacroAction 맨아래로이동(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 258, 257, MacroMouseActionType.WHEELDOWN, 200, status.getScreenList().size() < singleCount);
		}
		return new MacroMouseAction(basePoint, 246, 345, MacroMouseActionType.WHEELDOWN, 200, status.getScreenList().size() < singleCount);
	}
	private static MacroAction 화물차쟁탈전(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 315, 350);
		}
		return new MacroMouseAction(basePoint, 355, 567);
	}
	private static MacroAction 화물차선택(MacroStatus status, Point basePoint, int order) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return switch(order) {
			case 1->new MacroMouseAction(basePoint, 169, 255);
			case 2->new MacroMouseAction(basePoint, 209, 255);
			case 3->new MacroMouseAction(basePoint, 251, 255);
			case 4->new MacroMouseAction(basePoint, 290, 255);
			default->new MacroMouseAction(basePoint, 332, 255);
			};
		}
		return switch(order) {
		case 1->new MacroMouseAction(basePoint, 117, 415);
		case 2->new MacroMouseAction(basePoint, 186, 415);
		case 3->new MacroMouseAction(basePoint, 252, 415);
		case 4->new MacroMouseAction(basePoint, 314, 415);
		default->new MacroMouseAction(basePoint, 384, 415);
		};
	}
	private static MacroAction 화물차운송버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 249, 360);
		}
		return new MacroMouseAction(basePoint, 244, 585);
	}
	private static MacroAction 화물차운송안내닫기(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 341, 145);
		}
		return new MacroMouseAction(basePoint, 396, 235);
	}
	private static MacroAction 화물차갱신버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 345, 138);
		}
		return new MacroMouseAction(basePoint, 408, 225);
	}
	private static MacroAction 필수퀘스트지점(MacroStatus status, Point basePoint, int order) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return switch(order) {
			case 1->new MacroMouseAction(basePoint, 208, 145);
			case 2->new MacroMouseAction(basePoint, 295, 145);
			case 3->new MacroMouseAction(basePoint, 208, 180);
			default->new MacroMouseAction(basePoint, 295, 180);
			};
		}
		return switch(order) {
		case 1->new MacroMouseAction(basePoint, 180, 240);
		case 2->new MacroMouseAction(basePoint, 320, 240);
		case 3->new MacroMouseAction(basePoint, 180, 300);
		default->new MacroMouseAction(basePoint, 320, 300);
		};
	}
	private static MacroAction 필수퀘스트이동버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 249, 329);
		}
		return new MacroMouseAction(basePoint, 249, 544);
	}
	private static MacroAction 필수퀘스트구조하기버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 248, 343);
		}
		return new MacroMouseAction(basePoint, 242, 562);
	}
	private static MacroAction 레이더글자클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 247, 14);
		}
		return new MacroMouseAction(basePoint, 260, 30);
	}
	private static MacroAction 암흑잔재공격버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 250, 153);
		}
		return new MacroMouseAction(basePoint, 252, 256);
	}
	private static MacroAction 제국의유물공격버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 290, 334);
		}
		return new MacroMouseAction(basePoint, 280, 540);
	}
	private static MacroAction 보물연맹이동(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 311, 315);
		}
		return new MacroMouseAction(basePoint, 355, 515);
	}
	private static MacroAction 길드메뉴에서괴물클릭(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 191, 352);
		}
		return new MacroMouseAction(basePoint, 169, 575);
	}
	private static MacroAction 길드괴물레벨업버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 314, 370);
		}
		return new MacroMouseAction(basePoint, 354, 606);
	}
	private static MacroAction 길드괴물기부버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 251, 361);
		}
		return new MacroMouseAction(basePoint, 251, 588);
	}
	private static MacroAction 길드괴물기부닫기버튼(MacroStatus status, Point basePoint) {
		if(status.getScreenMode() == ScreenMode.SMALL) {
			return new MacroMouseAction(basePoint, 347, 58);
		}
		return new MacroMouseAction(basePoint, 403, 93);
	}
}





