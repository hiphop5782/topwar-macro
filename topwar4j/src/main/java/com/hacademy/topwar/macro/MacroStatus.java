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
	
	private int darkforceAttackCount = 5;
	private int darkforceMarchCount = 1;
	
	private List<Rectangle> screenList = new ArrayList<>();
	
	public static MacroStatus load() {
		File dir = new File(System.getProperty("user.home"), "tw-macro");
		File target = new File(dir, "status");
		return load(target);
	}
	public static MacroStatus load(File target) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(target));
			MacroStatus status = (MacroStatus) in.readObject();
			if(status.screenList == null) {
				status.screenList = new ArrayList<>();
			}
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
}
