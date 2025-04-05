package com.hacademy.topwar.util;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

import lombok.Getter;

//import org.bytedeco.opencv.opencv_core.Mat;
//import org.bytedeco.opencv.opencv_core.Point;
//import org.bytedeco.opencv.opencv_core.Size;

public class Mouse {
	private Robot robot;
	private Mouse() {
		try {
			robot = new Robot();
		}
		catch(AWTException e) {
			System.err.println("지원하지 않는 환경");
		}
	}
	
	public static Mouse create() {
		return new Mouse();
	}
	
	private int x, y;
	public static float delay = 0.1f;
	
	public Mouse hold() {
		return hold(delay);
	}
	public Mouse hold(float time) {
		try {
			Thread.sleep((long)(time * 1000));
		} catch (InterruptedException e) {
			throw new RuntimeException(Thread.currentThread().getName()+" is interrupted");
		}
		return this;
	}
	private void move() {
		robot.mouseMove(x, y);
	}
	public Mouse move(int x, int y) {
		this.x = x;
		this.y = y;
		move();
		return this;
	}
	public Mouse move(int x1, int y1, int x2, int y2, double segment) {
		double angle = Math.atan2(y2- y1, x2 - x1);
		int targetX = x1 + (int)(segment * Math.cos(angle));
		int targetY = y1 - (int)(segment + Math.sin(angle));
		
//		if(x1 < x2)
//			targetX = Math.min(targetX, x);
//		else
//			targetX = Math.max(targetX, x);
//		if(y1 < y2) 
//			targetY = Math.min(targetY, y);
//		else
//			targetY = Math.max(targetY, y);
		
		System.out.println("targetX = " + targetX+", targetY = " + targetY);
		return move(targetX, targetY);
	}
	public Mouse move(int x, int y, double durationSeconds) {
		int unitMillis = 50; //단위시간
		int segment = 10;
		while(!Thread.currentThread().isInterrupted()) {
			System.out.println("isInterrupted = " + Thread.currentThread().isInterrupted());
			Point p = MouseInfo.getPointerInfo().getLocation();
			double distance = Math.sqrt(Math.pow(x-p.x, 2) + Math.pow(y-p.y, 2));
			if(distance <= segment * 2) {
				move(x, y);
				break;
			}
			else {
				move(p.x, p.y, x, y, segment);
			}
			hold(unitMillis / 1000f);
		}
		return this;
	}
	public Mouse right(int value) {
		this.x += value;
		move();
		return this;
	}
	public Mouse left(int value) {
		this.x -= value;
		move();
		return this;
	}
	public Mouse up(int value) {
		this.y -= value;
		move();
		return this;
	}
	public Mouse down(int value) {
		this.y += value;
		move();
		return this;
	}
	public Mouse pushL() {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		return this;
	}
	public Mouse releaseL() {
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		return this;
	}
	public Mouse clickL() {
		return pushL().hold().releaseL();
	}
	public Mouse pushR() {
		robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		return this;
	}
	public Mouse releaseR() {
		robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		return this;
	}
	public Mouse clickL(int x, int y) {
//		System.out.println("Left Click ("+x+", "+y+")");
		return move(x, y).hold().clickL();
	}
	public Mouse clickR() {
		return pushR().hold().releaseR();
	}
	public Mouse clickR(int x, int y) {
		return move(x,y).hold().clickR();
	}
	public Mouse wheelUp(int offset) {
		moveWheel(-offset);
		return this;
	}
	public Mouse wheelDown(int offset) {
		moveWheel(offset);
		return this;
	}
	private Mouse moveWheel(int offset) {
		robot.mouseWheel(offset);
		return this;
	}
	
	public Mouse dragL(int x1, int y1, int x2, int y2, double durationSeconds) {
		return move(x1, y1).hold().pushL().hold().move(x2, y2, durationSeconds).hold(1.5f).releaseL();
	}
	
//	private Mat capture() throws IOException {
//		if(monitor == -1) {
//			return CaptureUtils.captureMatGrayscale(MonitorUtils.getMainMonitorBounds());
//		}
//		return CaptureUtils.captureMatGrayscale(MonitorUtils.getMonitorBounds(monitor));
//	}
//	public Mouse clickImgL(String path) throws IOException {
//		//System.out.println("<ClickImgL>");
//		//System.out.println("[path] " + path);
//		Mat find = ImageUtils.load(path);
//		Mat origin = capture();
//		Point point = ImageUtils.findImage(origin, find);
//		if(point == null) {
//			System.err.println("image not found");
//			return this;
//		}
//		System.out.println("Image detected at ("+point.x()+","+point.y()+")");
//		return clickL(point.x(), point.y());
//	}
//	
//	public Mouse clickImgR(String path) throws IOException {
//		Mat find = ImageUtils.load(path);
//		Mat origin = capture();
//		Point point = ImageUtils.findImage(origin, find);
//		if(point == null) {
//			System.err.println("image not found");
//			return this;
//		}
//		
//		Size size = find.size();
//		int x = point.x() + size.width()/2;
//		int y = point.y() + size.height()/2;
//		return clickR(x,y);
//	}
}
