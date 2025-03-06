package com.hacademy.topwar.macro;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MacroStatus implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int darkforceAttackCount = 1;
	private int darkforceMarchNumber = 1;
	private boolean potion = true;
	
	private int terror4kLevel = 5;
	private boolean terror4kManual = false;
	
	private List<Rectangle> screenList = new ArrayList<>();
	
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
	private boolean goldRequest = true;
	private boolean allianceDonation = true;
	private boolean productMaterial = true;
	private String productMaterialType = "강철";
	private boolean armyUnitTraining = true;
	private boolean navyUnitTraining = true;
	private boolean airforceUnitTraining = true;
	
	//weekly task
	private boolean weeklyDecorFreeToken = true;
	
	//기지 외부 작업
	private boolean oilFacility = true;
	private int oilFacilityLevel = 5;
	private boolean foodFacility = true;
	private int foodFacilityLevel = 5;
	private boolean odinFacility = true;
	private int odinFacilityLevel = 3;
	
	public static MacroStatus load() {
		File dir = new File(System.getProperty("user.home"), "tw-macro");
		File target = new File(dir, "status");
		return load(target);
	}
	public static MacroStatus load(File target) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(target));
			MacroStatus status = (MacroStatus) in.readObject();
			if(status.screenList == null) 
				status.screenList = new ArrayList<>();
			status.darkforceAttackCount = Math.max(status.darkforceAttackCount, 1);
			status.darkforceMarchNumber = Math.max(status.darkforceMarchNumber, 1);
			status.terror4kLevel = Math.max(status.terror4kLevel, 1);
			status.oilFacilityLevel = Math.max(status.oilFacilityLevel, 1);
			status.foodFacilityLevel = Math.max(status.foodFacilityLevel, 1);
			status.odinFacilityLevel = Math.max(status.odinFacilityLevel, 1);
			in.close();
			return status;
		}
		catch(Exception e) {}
		
		return new MacroStatus();
	}
	public void save() {
		File dir = new File(System.getProperty("user.home"), "tw-macro");
		dir.mkdirs();
		
		File target = new File(dir, "status");
		save(target);
	}
	public void save(File target) {
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(target));) {
			out.writeObject(this);
		}
		catch(Exception e) {}
	}
	
	public boolean hasAnyInternalTask() {
		return dailyVipReward || dailyBasketReward || dailySpecialReward
				|| dailyGemReward || dailyQuestReward || dailySandTraning
				|| dailyNormalIncrutAndSkill || dailyAdvancedIncruit
				|| dailyCrossBattle || goldRequest || allianceDonation
				|| productMaterial || armyUnitTraining || navyUnitTraining || airforceUnitTraining
				|| weeklyDecorFreeToken;
	}
	public boolean hasAnyExternalTask() {
		return odinFacility || foodFacility || oilFacility;
	}
}
