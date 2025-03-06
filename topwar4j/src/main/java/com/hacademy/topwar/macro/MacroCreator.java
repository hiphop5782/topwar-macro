package com.hacademy.topwar.macro;

import java.awt.Point;
import java.awt.Rectangle;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

import com.hacademy.topwar.constant.Button;
import com.hacademy.topwar.macro.action.MacroDelayAction;
import com.hacademy.topwar.util.ImageUtils;

public class MacroCreator {
	public static MacroDelayAction delay(long ms) {
		return new MacroDelayAction(ms/1000d);		
	}
	public static MacroTimelines darkforce(MacroStatus status) {
		MacroTimelines timelines = new MacroTimelines(true);
		for (Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.암흑매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		return timelines;
	}
	public static MacroTimelines darkforce(MacroStatus status, int durationSecond) {
		MacroTimelines timelines = darkforce(status);
		long ms = timelines.getDuration();
		long remain = durationSecond * 1000L - ms;
		if(remain > 0) {//마지막 잔여 딜레이 추가
			timelines.last().add(delay(remain));
		}
		return timelines;
	}
	public static MacroTimelines warhammer4k(MacroStatus status) {
		MacroTimelines timelines = new MacroTimelines(true);
		for (Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.워해머매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		return timelines;
	}
	public static MacroTimelines warhammer4k(MacroStatus status, int durationSecond) {
		MacroTimelines timelines = warhammer4k(status);
		long ms = timelines.getDuration();
		long remain = durationSecond * 1000L - ms;
		if(remain > 0) {//마지막 잔여 딜레이 추가
			timelines.last().add(delay(remain));
		}
		return timelines;
	}
	public static MacroTimelines terror4k(MacroStatus status) {
		MacroTimelines timelines = new MacroTimelines(false);
		for (Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.테러매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		return timelines;
	}
	public static MacroTimelines terror4k(MacroStatus status, int durationSecond) {
		MacroTimelines timelines = terror4k(status);
		if(!status.isTerror4kManual()) {//집결 시
			long ms = timelines.getDuration();
			long remain = durationSecond * 1000L - ms;
			if(remain > 0) {//마지막 잔여 딜레이 추가
				timelines.last().add(delay(remain));
			}
		}
		return timelines;
	}
	public static MacroTimelinesGroup task(MacroTimelinesGroup timelinesGroup, MacroStatus status) throws Exception {
		return task(timelinesGroup, status, false);
	}
	public static MacroTimelinesGroup task(MacroTimelinesGroup timelinesGroup, MacroStatus status, boolean considerWeek) throws Exception {
		timelinesGroup.clear();
		
		String week = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
		
		//기지 내부로 모두 이동시키기(화면 반응속도를 향상시키기 위해)
		//- 레이더 버튼이 보이면 월드 맵에 위치한 것으로 판정
		if(status.hasAnyInternalTask()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				Point rader = ImageUtils.searchButton(screenRect, Button.RADER);
				if(rader != null) {
					MacroTimeline timeline = MacroTimelineFactory.월드기지전환(status, screenRect.getLocation());
					timelines.add(timeline);
				}
			}
			if(timelines.size() > 0) timelinesGroup.add(timelines);
		}

		if (status.isDailyVipReward()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.VIP보상받기매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isDailyBasketReward()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.장바구니매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isDailySpecialReward()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.패키지무료보상매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isDailySandTraning()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.사판훈련매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isDailyNormalIncrutAndSkill()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.일반모집스킬모집매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isDailyAdvancedIncruit()) {
			int count = 2;
			if(considerWeek == true) {
				count = switch(week) {
				case "월","화"->15;
				case "수"->30;
				default->2;
				};
			}
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.고급모집매크로(status, screenRect.getLocation(), count);
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isDailyQuestReward()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.일일임무매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isGoldRequest()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.골드지원요청(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isDailyCrossBattle()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.크로스패배매크로(status, screenRect.getLocation(), 10);
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isDailyGemReward()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.무료보석수집매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isArmyUnitTraining()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.육군훈련(status, screenRect.getLocation(), 15);
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isNavyUnitTraining()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.해군훈련(status, screenRect.getLocation(), 15);
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isAirforceUnitTraining()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.공군훈련(status, screenRect.getLocation(), 15);
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}

		// 주간
		if (status.isWeeklyDecorFreeToken()) {
			if(considerWeek==false || week.equals("월")) {
				MacroTimelines timelines = new MacroTimelines(false);
				for (Rectangle screenRect : status.getScreenList()) {
					MacroTimeline timeline = MacroTimelineFactory.주간장식세트무료쿠폰매크로(status, screenRect.getLocation());
					timelines.add(timeline);
				}
				timelinesGroup.add(timelines);
			}
		}

		//외부 작업이 있으면 기지 전환
		if(status.hasAnyExternalTask()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				Point rader = ImageUtils.searchButton(screenRect, Button.RADER);
				if(rader == null) {
					MacroTimeline timeline = MacroTimelineFactory.월드기지전환(status, screenRect.getLocation());
					timelines.add(timeline);
				}
			}
			timelinesGroup.add(timelines);
		}
		if(status.isAllianceDonation()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.길드기부매크로(status, screenRect.getLocation(), 10);
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isProductMaterial()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.재료생산매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}

		// 시설
		if (status.isOilFacility()) {
			MacroTimelines timelines = new MacroTimelines(true);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.석유시설매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isFoodFacility()) {
			MacroTimelines timelines = new MacroTimelines(true);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.식량시설매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if (status.isOdinFacility()) {
			MacroTimelines timelines = new MacroTimelines(true);
			for (Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.오딘시설매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		
		return timelinesGroup;
	}
}












