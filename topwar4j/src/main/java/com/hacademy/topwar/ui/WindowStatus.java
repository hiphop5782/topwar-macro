package com.hacademy.topwar.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import lombok.Data;

@Data
public class WindowStatus implements Serializable{
	private static final long serialVersionUID = 1L;
	private int x, y;
	private boolean mini;
	
	public static void save(MainFrame frame) {
		WindowStatus status = new WindowStatus();
		status.x = frame.getX();
		status.y = frame.getY();
		status.mini = frame.isMini();
		File dir = new File(System.getProperty("user.home"), "tw-macro");
		dir.mkdirs();
		File target = new File(dir, "window");
		try (
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(target)));
		){
			out.writeObject(status);
		}
		catch(Exception e) {
			System.err.println("WindowStatus 저장 오류");
		}
	}

	public static WindowStatus load() {
		File dir = new File(System.getProperty("user.home"), "tw-macro");
		File target = new File(dir, "window");
		if(target.exists()) {
			try (
				ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(target)));
			){
				WindowStatus status = (WindowStatus) in.readObject();
				return status;
			}
			catch(Exception e) {}
		}
		return null;
	}
}
