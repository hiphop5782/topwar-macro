package com.hacademy.topwar.macro;

import java.awt.Point;
import java.awt.Rectangle;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import com.hacademy.topwar.macro.action.MacroDelayAction;
import com.hacademy.topwar.macro.action.MacroMouseAction;
import com.hacademy.topwar.macro.action.MacroTypingAction;
import com.hacademy.topwar.ui.LogDialog;
import com.hacademy.topwar.util.RectData;

public class MacroCreator {
	public static MacroDelayAction delay(long ms) {
		return new MacroDelayAction(ms/1000d);		
	}
	public static MacroTimelines darkforce(MacroStatus status) throws Exception {
		MacroTimelines timelines = new MacroTimelines("암흑", true);
		for (RectData rectData : status.getScreenList()) {
			if(rectData.active == false) continue;
			Rectangle screenRect = rectData.toRectangle();
//			Point rader = ImageUtils.searchRaderButton(screenRect);
//			if(rader == null) {
//				MacroTimeline timeline = MacroTimelineFactory.월드기지전환(status, screenRect.getLocation());
//				timelines.add(timeline);
//			}
			
			MacroTimeline timeline = MacroTimelineFactory.암흑매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		return timelines;
	}
	public static MacroTimelines darkforceLoop(MacroStatus status) throws Exception {
		MacroTimelines timelines = darkforce(status);
		long ms = timelines.getDuration();
		//Delay delay = status.getDarkforceAttackCount() == 1 ? Delay.DARKFORCE1 : Delay.DARKFORCE5;
		//long remain = delay.getDurationMillis() - ms;
		long remain = status.getDarkforceDuration() * 1000L;
		if(remain > 0) {//마지막 잔여 딜레이 추가
			timelines.last().add(delay(remain));
		}
		return timelines;
	}
	public static MacroTimelines warhammer4k(MacroStatus status) throws Exception {
		MacroTimelines timelines = new MacroTimelines("워해머", true);
		for (RectData rectData : status.getScreenList()) {
			if(rectData.active == false) continue;
			Rectangle screenRect = rectData.toRectangle();
//			Point rader = ImageUtils.searchButton(screenRect, Button.RADER);
//			if(rader == null) {
//				MacroTimeline timeline = MacroTimelineFactory.월드기지전환(status, screenRect.getLocation());
//				timelines.add(timeline);
//			}
			
			MacroTimeline timeline = MacroTimelineFactory.워해머매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		return timelines;
	}
	public static MacroTimelines warhammer4k(MacroStatus status, int durationSecond) throws Exception {
		MacroTimelines timelines = warhammer4k(status);
		long ms = timelines.getDuration();
		long remain = durationSecond * 1000L - ms;
		if(remain > 0) {//마지막 잔여 딜레이 추가
			timelines.last().add(delay(remain));
		}
		return timelines;
	}
	public static MacroTimelines terror4k(MacroStatus status) throws Exception {
		MacroTimelines timelines = new MacroTimelines("테러", false);
		for (RectData rectData : status.getScreenList()) {
			if(rectData.active == false) continue;
			Rectangle screenRect = rectData.toRectangle();
//			Point rader = ImageUtils.searchButton(screenRect, Button.RADER);
//			if(rader == null) {
//				MacroTimeline timeline = MacroTimelineFactory.월드기지전환(status, screenRect.getLocation());
//				timelines.add(timeline);
//			}
			
			MacroTimeline timeline = MacroTimelineFactory.테러매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		return timelines;
	}
	public static MacroTimelines terror4k(MacroStatus status, int durationSecond) throws Exception {
		MacroTimelines timelines = terror4k(status);
		if(!status.isTerror4kManual()) {//집결 시
			long ms = timelines.getDuration();
			long remain = durationSecond * 1000L - ms;
			if(remain > 0) {//마지막 잔여 딜레이 추가
				timelines.last().add(delay(remain));
			}
		}
		else {//공격일 때도 딜레이 추가
			timelines.last().add(delay(5000L));
		}
		return timelines;
	}
	public static void moveIntoBase(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
//		MacroTimelines timelines = new MacroTimelines("기지내부로이동", false);
//		for (Rectangle screenRect : status.getScreenList()) {
//			Point rader = ImageUtils.searchButton(Area.RADER.getRect(), Button.RADER);
//			if(rader != null) {
//				MacroTimeline timeline = MacroTimelineFactory.월드기지전환(status, screenRect.getLocation());
//				timelines.add(timeline);
//			}
//		}
//		if(timelines.size() > 0) timelinesGroup.add(timelines);
	}
	public static void moveOutofBase(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
//		MacroTimelines timelines = new MacroTimelines("기지외부로이동", false);
//		for (Rectangle screenRect : status.getScreenList()) {
//			Point rader = ImageUtils.searchButton(Area.RADER.getRect(), Button.RADER);
//			if(rader == null) {
//				MacroTimeline timeline = MacroTimelineFactory.월드기지전환(status, screenRect.getLocation());
//				timelines.add(timeline);
//			}
//		}
//		if(timelines.size() > 0) timelinesGroup.add(timelines);
	}
	public static void VIP보상(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if (status.isDailyVipReward()) {
			moveIntoBase(timelinesGroup, status);//기지내부로이동
			MacroTimelines timelines = new MacroTimelines("VIP보상", false);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.VIP보상받기매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 장바구니(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if (status.isDailyBasketReward()) {
			moveIntoBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("장바구니", false);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.장바구니매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 특별패키지(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if (status.isDailySpecialReward()) {
			moveIntoBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("특별패키지", false);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.패키지무료보상매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 사판훈련(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if (status.isDailySandTraning()) {
			moveIntoBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("사판훈련", false);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.사판훈련매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 일반스킬모집(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if (status.isDailyNormalIncrutAndSkill()) {
			moveIntoBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("일반/스킬모집", false);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.일반모집스킬모집매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 고급모집(MacroTimelinesGroup timelinesGroup, MacroStatus status, boolean considerWeek) throws Exception {
		if (status.isDailyAdvancedIncruit()) {
			moveIntoBase(timelinesGroup, status);
			String week = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
			int count = considerWeek ? switch(week) {
										case "월","화"->15;
										case "수"->30;
										default->2;
										} : 2;
			MacroTimelines timelines = new MacroTimelines("고급모집", false);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.고급모집매크로(status, screenRect.getLocation(), count);
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 일일임무(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if (status.isDailyQuestReward()) {
			moveIntoBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("일일임무", false);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.일일임무매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 골드지원(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if (status.isGoldRequest()) {
			moveIntoBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("골드지원", false);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.골드지원요청(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 크로스패배(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if (status.isDailyCrossBattle()) {
			moveIntoBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("크로스패배", false);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.크로스패배매크로(status, screenRect.getLocation(), 10);
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 무료보석(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if (status.isDailyGemReward()) {
			moveIntoBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("무료보석", false);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.무료보석수집매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
//	public static void 육군훈련(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
//		if (status.isArmyUnitTraining()) {
//			moveIntoBase(timelinesGroup, status);
//			MacroTimelines timelines = new MacroTimelines("육군훈련", false);
//			for (Rectangle screenRect : status.getScreenList()) {
//				MacroTimeline timeline = MacroTimelineFactory.육군훈련(status, screenRect.getLocation(), 15);
//				timelines.add(timeline);
//			}
//			timelinesGroup.add(timelines);
//		}
//	}
//	public static void 해군훈련(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
//		if (status.isNavyUnitTraining()) {
//			moveIntoBase(timelinesGroup, status);
//			MacroTimelines timelines = new MacroTimelines("해군훈련", false);
//			for (Rectangle screenRect : status.getScreenList()) {
//				MacroTimeline timeline = MacroTimelineFactory.해군훈련(status, screenRect.getLocation(), 15);
//				timelines.add(timeline);
//			}
//			timelinesGroup.add(timelines);
//		}
//	}
//	public static void 공군훈련(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
//		if (status.isAirforceUnitTraining()) {
//			moveIntoBase(timelinesGroup, status);
//			MacroTimelines timelines = new MacroTimelines("공군훈련", false);
//			for (Rectangle screenRect : status.getScreenList()) {
//				MacroTimeline timeline = MacroTimelineFactory.공군훈련(status, screenRect.getLocation(), 15);
//				timelines.add(timeline);
//			}
//			timelinesGroup.add(timelines);
//		}
//	}
	public static void 주간장식세트무료토큰(MacroTimelinesGroup timelinesGroup, MacroStatus status, boolean considerWeek) throws Exception {
		if (status.isWeeklyDecorFreeToken()) {
			String week = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
			if(considerWeek==false || week.equals("월")) {
				MacroTimelines timelines = new MacroTimelines("주간장식세트무료토큰", false);
				for (RectData rectData : status.getScreenList()) {
					if(rectData.active == false) continue;
					Rectangle screenRect = rectData.toRectangle();
					MacroTimeline timeline = MacroTimelineFactory.주간장식세트무료쿠폰매크로(status, screenRect.getLocation());
					timelines.add(timeline);
				}
				timelinesGroup.add(timelines);
			}
		}
	}
	public static void 길드기부(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if(status.isAllianceDonation()) {
			moveIntoBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("길드기부", false);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.길드기부매크로(status, screenRect.getLocation(), 10);
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 재료생산(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if(status.isProductMaterial()) {
			moveIntoBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("재료생산", false);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.재료생산매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 석유시설(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if (status.isOilFacility()) {
			moveOutofBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("석유시설", true);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.석유시설매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 식량시설(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if (status.isFoodFacility()) {
			moveOutofBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("식량시설", true);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.식량시설매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static void 오딘시설(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		if (status.isOdinFacility()) {
			moveOutofBase(timelinesGroup, status);
			MacroTimelines timelines = new MacroTimelines("오딘시설", true);
			for (RectData rectData : status.getScreenList()) {
				if(rectData.active == false) continue;
				Rectangle screenRect = rectData.toRectangle();
				MacroTimeline timeline = MacroTimelineFactory.오딘시설매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
	}
	public static MacroTimelinesGroup task(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		return task(timelinesGroup, status, false);
	}
	public static MacroTimelinesGroup task(MacroTimelinesGroup timelinesGroup, MacroStatus status, boolean considerWeek) throws Exception {
		timelinesGroup.clear();
		
		VIP보상(timelinesGroup, status);
		장바구니(timelinesGroup, status);
		특별패키지(timelinesGroup, status);
		사판훈련(timelinesGroup, status);
		일반스킬모집(timelinesGroup, status);
		고급모집(timelinesGroup, status, considerWeek);
		일일임무(timelinesGroup, status);
//		골드지원(timelinesGroup, status);
		크로스패배(timelinesGroup, status);
		무료보석(timelinesGroup, status);

		// 주간
		주간장식세트무료토큰(timelinesGroup, status, considerWeek);
		
		길드기부(timelinesGroup, status);
		재료생산(timelinesGroup, status);
//		육군훈련(timelinesGroup, status);
//		해군훈련(timelinesGroup, status);
//		공군훈련(timelinesGroup, status);
		
		// 시설
		석유시설(timelinesGroup, status);
		식량시설(timelinesGroup, status);
		오딘시설(timelinesGroup, status);    
		
		return timelinesGroup;
	}
	
	public static MacroTimelinesGroup facility(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		timelinesGroup.clear();
		
		석유시설(timelinesGroup, status);
		식량시설(timelinesGroup, status);
		오딘시설(timelinesGroup, status);
		
		return timelinesGroup;
	}
	public static MacroTimelinesGroup notice(MacroTimelinesGroup timelinesGroup, MacroStatus status, 
			int screenNumber, int period, String text) {
		timelinesGroup.clear();
		
		RectData rectData = status.getScreenList().get(screenNumber-1);
		Rectangle baseRect = rectData.toRectangle();
		Point basePoint = baseRect.getLocation();
		
		MacroTimelines timelines = new MacroTimelines("텍스트 알림", true);
		
		long periodMillis = period * 60L * 1000L;
		
		MacroTimeline timeline = new MacroTimeline();
		text.lines().forEach(line->{
			timeline.add(new MacroMouseAction(basePoint, 226, 653));
			timeline.add(new MacroMouseAction(basePoint, 226, 653));
			timeline.add(new MacroTypingAction(line));
			timeline.add(delay(500L));
		});
		timeline.add(delay(periodMillis));
		
		timelines.add(timeline);
		
		timelinesGroup.add(timelines);
		return timelinesGroup;
	}
}












