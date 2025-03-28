package com.hacademy.topwar.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Keyboard {
	private Robot robot;
	private Keyboard() {
		try {
			robot = new Robot();
		}
		catch(AWTException e) {
			System.err.println("지원하지 않는 환경");
		}
	}
	
	public static Keyboard create() {
		return new Keyboard();
	}
	
	public Keyboard type(String text) {
		for(int i=0; i < text.length(); i++) {
			char ch = text.charAt(i);
			type(ch).hold();
		}
		return this;
	}
	public Keyboard type(int keyCode) {
		return press(keyCode).release(keyCode);
	}
	public Keyboard press(int keyCode) {
		robot.keyPress(keyCode);
		return this;
	}
	public Keyboard release(int keyCode) {
		robot.keyRelease(keyCode);
		return this;
	}
	public Keyboard backspace() {
		return backspace(1);
	}
	public Keyboard backspace(int count) {
		for(int i=0; i < count; i++) {
			type(KeyEvent.VK_BACK_SPACE);
		}
		return this;
	}
	
	private float delay = 0.25f;
	
	public Keyboard hold() {
		return hold(delay);
	}
	public Keyboard hold(float time) {
		try {
			Thread.sleep((long)(time * 1000));
		} catch (InterruptedException e) {
			throw new RuntimeException(Thread.currentThread().getName()+" is interrupted");
		}
		return this;
	}
}
