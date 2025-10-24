package com.hacademy.topwar.macro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hacademy.topwar.util.RectData;

import lombok.Data;

@Data
public class MacroStatus implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int darkforceAttackCount = 1;
	private String darkforceLevel = "random";
	private int darkforceDuration = 300;
	private int darkforceMarchNumber = 1;
	
	private String warhammerLevel = "random";
	private String warhammerType = "워해머-4K";
	private int warhammerDuration = 60;
	
	private boolean potion = true;//물약 사용
	private boolean materialRequest = false;//재료 요청
	
	private int terror4kLevel = 5;
	private boolean terror4kManual = false;
	
	private List<RectData> screenList = new ArrayList<>();
	
	//기지 내부 작업
	//daily task
	private boolean dailyVipReward = true;
	private boolean dailyBasketReward = true;
	private boolean dailySpecialReward = true;
	private boolean dailyGemReward = true;
	private boolean dailyQuestReward = true;
	private boolean dailySandTraning = true;
	private boolean dailyNormalIncrutAndSkill = false;
	private boolean dailyAdvancedIncruit = false;
	private boolean dailyCrossBattle = true;
	private boolean allianceDonation = true;
	private boolean productMaterial = true;
	private String productMaterialType = "강철";
//	private boolean armyUnitTraining = true;
//	private boolean navyUnitTraining = true;
//	private boolean airforceUnitTraining = true;
	private boolean dailyGoldRequest = true;
	
	//weekly task
	private boolean weeklyDecorFreeToken = true;
	
	//기지 외부 작업
	private boolean oilFacility = true;
	private int oilFacilityLevel = 5;
	private boolean foodFacility = true;
	private int foodFacilityLevel = 5;
	private boolean odinFacility = true;
	private int odinFacilityLevel = 3;
}
