package com.hacademy.topwar.macro;

import java.awt.Point;

import com.hacademy.topwar.macro.action.MacroAction;
import com.hacademy.topwar.macro.action.MacroDelayAction;
import com.hacademy.topwar.macro.action.MacroMouseAction;
import com.hacademy.topwar.macro.action.MacroMouseActionType;

public class MacroTimelineFactory {
	public static MacroTimeline 암흑매크로(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		timeline.add(돋보기버튼클릭(status, basePoint));//적군 검색(+다른창닫기)
		timeline.add(돋보기버튼클릭(status, basePoint));//적군 검색
		timeline.add(적군탭선택(status, basePoint));//적군
		timeline.add(암흑오딘유닛선택(status, basePoint));//암흑 오딘
		
		//레벨선택은 랜덤하게 설정(98~102)
		timeline.add(랜덤암흑유닛선택(status, basePoint));//레벨선택
		
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
		timeline.add(워해머선택(status, basePoint));//워해머-4K
		
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
		timeline.add(주간카드탭(status, basePoint));
		timeline.add(randomDelay(status, 1, 2));
		
		for(int i=0; i < 20; i++) {
			timeline.add(주간카드무료다이아수령(status, basePoint));
			timeline.add(randomDelay(status, 16, 18));
		}
		
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
		for(int i=1; i < status.getOilFacilityLevel(); i++) {
			timeline.add(레벨더하기버튼클릭(status, basePoint));
		}
		timeline.add(자원지검색버튼(status, basePoint));
		timeline.add(randomDelay(status, 3, 5));
		
		timeline.add(화면중앙클릭(status, basePoint));
		timeline.add(채집시설버튼클릭(status, basePoint));
		timeline.add(빠른출전버튼클릭(status, basePoint));
		timeline.add(슬롯4번선택(status, basePoint));
		timeline.add(슬롯5번선택(status, basePoint));
		timeline.add(슬롯6번선택(status, basePoint));
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
		for(int i=1; i < status.getFoodFacilityLevel(); i++) {
			timeline.add(레벨더하기버튼클릭(status, basePoint));
		}
		timeline.add(자원지검색버튼(status, basePoint));
		timeline.add(randomDelay(status, 3, 5));
		
		timeline.add(화면중앙클릭(status, basePoint));
		timeline.add(채집시설버튼클릭(status, basePoint));
		timeline.add(빠른출전버튼클릭(status, basePoint));
		timeline.add(슬롯4번선택(status, basePoint));
		timeline.add(슬롯5번선택(status, basePoint));
		timeline.add(슬롯6번선택(status, basePoint));
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
		timeline.add(크로스훈련으로이동(status, basePoint));
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
		timeline.add(길드지원(status, basePoint));
		timeline.add(길드지원요청취소(status, basePoint));
		timeline.add(길드지원요청취소확인(status, basePoint));
		timeline.add(길드지원요청시작(status, basePoint));
		timeline.add(길드지원골드선택(status, basePoint));
		timeline.add(길드지원요청완료(status, basePoint));
		timeline.add(randomDelay(status, 0.5, 0.7));
		timeline.add(길드지원요청취소(status, basePoint));
		timeline.add(길드지원요청취소확인(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 기지내부로이동(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(월드기지전환버튼(status, basePoint));
		timeline.add(randomDelay(status, 1, 1.5));
		timeline.add(월드기지전환버튼(status, basePoint));
		timeline.add(randomDelay(status, 1, 1.5));
		timeline.add(내기지선택(status, basePoint));
		timeline.add(randomDelay(status, 1, 1.5));
		timeline.add(내기지입장(status, basePoint));
		timeline.add(randomDelay(status, 3, 3.5));
		
		return timeline;
	}
	public static MacroTimeline 기지내부에서외부로이동(MacroStatus status, Point basePoint) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(월드기지전환버튼(status, basePoint));
		timeline.add(randomDelay(status, 1, 1.5));
		
		return timeline;
	}
	public static MacroTimeline 육군훈련(MacroStatus status, Point basePoint, int count) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(좌측사이드메뉴선택(status, basePoint));
		timeline.add(randomDelay(status, 1.2, 1.5));
		timeline.add(좌측사이드메뉴육군훈련버튼(status, basePoint));
		timeline.add(randomDelay(status, 1.2, 1.5));
		timeline.add(유닛생산취소버튼(status, basePoint));
		timeline.add(randomDelay(status, 0.5, 0.7));
		timeline.add(유닛생산훈련소클릭(status, basePoint));
		
		for(int i=0; i < count; i++) {
			timeline.add(유닛생산버튼클릭(status, basePoint));
//			timeline.add(randomDelay(status, 0.5, 0.7));
		}
		
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 해군훈련(MacroStatus status, Point basePoint, int count) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(좌측사이드메뉴선택(status, basePoint));
		timeline.add(randomDelay(status, 1.2, 1.5));
		timeline.add(좌측사이드메뉴해군훈련버튼(status, basePoint));
		timeline.add(randomDelay(status, 1.2, 1.5));
		timeline.add(유닛생산취소버튼(status, basePoint));
		timeline.add(randomDelay(status, 0.5, 0.7));
		timeline.add(유닛생산훈련소클릭(status, basePoint));
		
		for(int i=0; i < count; i++) {
			timeline.add(유닛생산버튼클릭(status, basePoint));
//			timeline.add(randomDelay(status, 0.5, 0.7));
		}
		
		timeline.add(뒤로가기(status, basePoint));
		
		return timeline;
	}
	public static MacroTimeline 공군훈련(MacroStatus status, Point basePoint, int count) {
		MacroTimeline timeline = new MacroTimeline();
		
		timeline.add(좌측사이드메뉴선택(status, basePoint));
		timeline.add(randomDelay(status, 1.2, 1.5));
		timeline.add(좌측사이드메뉴공군훈련버튼(status, basePoint));
		timeline.add(randomDelay(status, 1.2, 1.5));
		timeline.add(유닛생산취소버튼(status, basePoint));
		timeline.add(randomDelay(status, 0.5, 0.7));
		timeline.add(유닛생산훈련소클릭(status, basePoint));
		
		for(int i=0; i < count; i++) {
			timeline.add(유닛생산버튼클릭(status, basePoint));
//			timeline.add(randomDelay(status, 0.5, 0.7));
		}
		
		timeline.add(뒤로가기(status, basePoint));
		
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
	private static MacroAction 돋보기버튼클릭(MacroStatus status, Point basePoint) {
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
	private static MacroAction 워해머선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 134, 437);
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
	private static MacroAction 처음탭으로이동(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 225, 75, MacroMouseActionType.WHEELDOWN, 20);
	}
	private static MacroAction 마지막탭으로이동(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 225, 75, MacroMouseActionType.WHEELUP, 200);
	}
	private static MacroAction 장바구니탭선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 225, 75);
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
		return new MacroMouseAction(basePoint, 377, 130);
	}
	private static MacroAction 일일임무버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 77, 580);
	}
	private static MacroAction 일일임무일괄수령버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 348, 135);
	}
	private static MacroAction 일일임무일괄수령진행버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 250, 550);
	}
	private static MacroAction 일일임무일괄수령확인버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 250, 615);
	}
	private static MacroAction 사판훈련으로이동(MacroStatus status, Point basePoint) {
		//return new MacroMouseAction(basePoint, 354, 640, MacroMouseActionType.WHEELDOWN, 10);
		return new MacroMouseAction(basePoint, 354, 640, 354, 570, MacroMouseActionType.DRAG);
	}
	private static MacroAction 사판훈련(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 353, 640);
	}
	private static MacroAction 사판훈련도전(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 200, 663);
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
		return new MacroMouseAction(basePoint, 349, 660, MacroMouseActionType.WHEELDOWN, 300);
	}
	private static MacroAction 섬대작전선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 349, 660);
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
	private static MacroAction 장식세트상점탭클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 74, 70);
	}
	private static MacroAction 장식세트토큰받기버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 352, 663);
	}
	private static MacroAction 장식세트무료토큰받기클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 162, 137);
	}
	private static MacroAction 자원지탭선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 205, 319);
	}
	private static MacroAction 유전선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 123, 435);
	}
	private static MacroAction 농지선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 247, 435);
	}
	private static MacroAction 오딘광맥선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 383, 435);
	}
	private static MacroAction 레벨더하기버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 354, 595);
	}
	private static MacroAction 레벨빼기버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 146, 595);
	}
	private static MacroAction 자원지검색버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 244, 644);
	}
	private static MacroAction 화면중앙클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 246, 345);
	}
	private static MacroAction 채집시설버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 189, 267);
	}
	private static MacroAction 빠른출전버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 467, 548);
	}
	private static MacroAction 자원건설확인버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 248, 456);
	}
	private static MacroAction 자원지마지막탭으로이동(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 247, 435, MacroMouseActionType.WHEELUP, 100);
	}
	private static MacroAction 일반모집탭선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 80, 645);
	}
	private static MacroAction 스킬모집탭선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 414, 645);
	}
	private static MacroAction 크로스훈련구매창X버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 417, 193);
	}
	private static MacroAction 크로스훈련X버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 409, 111);
	}
	private static MacroAction 크로스훈련뒤로버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 251, 604);
	}
	private static MacroAction 전투버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 244, 285);
	}
	private static MacroAction 첫번째유닛선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 29, 643);
	}
	private static MacroAction 슬롯9번선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 318, 538);
	}
	private static MacroAction 슬롯8번선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 202, 519);
	}
	private static MacroAction 슬롯7번선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 89, 497);
	}
	private static MacroAction 슬롯6번선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 363, 481);
	}
	private static MacroAction 슬롯5번선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 249, 458);
	}
	private static MacroAction 슬롯4번선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 129, 439);
	}
	private static MacroAction 슬롯3번선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 404, 420);
	}
	private static MacroAction 슬롯2번선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 289, 400);
	}
	private static MacroAction 슬롯1번선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 181, 376);
	}
	private static MacroAction 크로스훈련적선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 365, 550);
	}
	private static MacroAction 크로스훈련도전버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 251, 653);
	}
	private static MacroAction 크로스훈련선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 353, 228);
	}
	private static MacroAction 크로스훈련으로이동(MacroStatus status, Point basePoint) {
		//return new MacroMouseAction(basePoint, 351, 623, MacroMouseActionType.WHEELUP, 20);
		return new MacroMouseAction(basePoint, 351, 660, 351, 210, MacroMouseActionType.DRAG);
	}
	private static MacroAction 길드메뉴(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 471, 469);
	}
	private static MacroAction 길드지원(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 348, 398);
	}
	private static MacroAction 길드지원요청시작(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 374, 603);
	}
	private static MacroAction 길드지원요청취소(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 374, 603);
	}
	private static MacroAction 길드지원요청취소확인(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 339, 451);
	}
	private static MacroAction 길드지원골드선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 207, 379);
	}
	private static MacroAction 길드지원요청완료(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 254, 500);
	}
	private static MacroAction 최소레벨선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 175, 594);
	}
	private static MacroAction 좌측사이드메뉴선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 10, 349);
	}
	private static MacroAction 좌측사이드메뉴육군훈련버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 160, 229);
	}
	private static MacroAction 좌측사이드메뉴해군훈련버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 160, 267);
	}
	private static MacroAction 좌측사이드메뉴공군훈련버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 160, 302);
	}
	private static MacroAction 좌측사이드메뉴재료생산버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 160, 441);
	}
	private static MacroAction 좌측사이드메뉴길드기부버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 160, 476);
	}
	private static MacroAction 월드기지전환버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 409, 665);
	}
	private static MacroAction 내기지선택(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 248, 347);
	}
	private static MacroAction 내기지입장(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 171, 489);
	}
	private static MacroAction 유닛생산취소버튼(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 327, 424);
	}
	private static MacroAction 유닛생산훈련소클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 250, 366);
	}
	private static MacroAction 유닛생산버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 250, 329);
	}
	private static MacroAction 길드과학기술기부버튼클릭(MacroStatus status, Point basePoint) {
		return new MacroMouseAction(basePoint, 336, 515);
	}
	private static MacroAction 재료생산클릭(MacroStatus status, Point basePoint) {
		switch(status.getProductMaterialType()) {
		case "강철": 		return new MacroMouseAction(basePoint, 117, 522);
		case "나사":		return new MacroMouseAction(basePoint, 206, 522);
		case "트랜지스터":	return new MacroMouseAction(basePoint, 290, 522);
		case "고무":		return new MacroMouseAction(basePoint, 379, 522);
		case "텅스텐":	return new MacroMouseAction(basePoint, 122, 606);
		case "배터리":	return new MacroMouseAction(basePoint, 203, 606);
		case "유리":		return new MacroMouseAction(basePoint, 294, 606);
		default:		return new MacroMouseAction(basePoint, 117, 522);
		}
	}
	
}





