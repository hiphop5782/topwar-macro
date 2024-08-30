package com.hacademy.topwar.macro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import lombok.Data;

@Data
public class MacroStatus implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int darkforceAttackCount = 5;
	private int darkforceMarchCount = 1;
	
	public static MacroStatus load() {
		try {
			File dir = new File(System.getProperty("user.home"), "tw-macro");
			File target = new File(dir, "status");
			if(target.exists()) {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(target));
				MacroStatus status = (MacroStatus) in.readObject();
				in.close();
				return status;
			}
		}
		catch(Exception e) {}
		
		return new MacroStatus();
	}
	public void save() {
		File dir = new File(System.getProperty("user.home"), "tw-macro");
		dir.mkdirs();
		
		File target = new File(dir, "status");
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(target));) {
			out.writeObject(this);
		}
		catch(Exception e) {}
	}
}
