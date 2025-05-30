package com.hacademy.topwar.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;

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
	
	private static GlobalKeyboardHook hook = null;
	private static final GlobalKeyListener listener = new GlobalKeyAdapter() {
		@Override
		public void keyReleased(GlobalKeyEvent event) {
			switch(event.getVirtualKeyCode()) {
			case GlobalKeyEvent.VK_ESCAPE:
				System.exit(0);
			}
		}
	};
	public static void enableEscToQuit() {
		if(hook != null) return;
		hook = new GlobalKeyboardHook(false);
		hook.addKeyListener(listener);
	}
	public static void disableEscToQuit() {
		if(hook == null) return;
		hook.removeKeyListener(listener);
		hook = null;
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
	
	public Keyboard saveToClipboard(String text) {
		StringSelection selection = new StringSelection(text);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		return this;
	}
	
	public Keyboard copy() {
		return press(KeyEvent.VK_CONTROL).hold()
					.press(KeyEvent.VK_C).hold()
					.release(KeyEvent.VK_C).hold()
					.release(KeyEvent.VK_CONTROL);
	}
	public Keyboard paste() {
		return press(KeyEvent.VK_CONTROL).hold()
					.press(KeyEvent.VK_V).hold()
					.release(KeyEvent.VK_V).hold()
					.release(KeyEvent.VK_CONTROL);
	}
	public Keyboard enter() {
		return type(KeyEvent.VK_ENTER);
	}
}
