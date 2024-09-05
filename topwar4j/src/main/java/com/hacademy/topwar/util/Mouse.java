package com.hacademy.topwar.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;

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
	private float delay = 0.1f;
	private int monitor = -1;
	
	public Mouse monitor(int monitor) {
		this.monitor = monitor;
		return this;
	}
	public Mouse hold() {
		return hold(delay);
	}
	public Mouse hold(float time) {
		try {
			Thread.sleep((long)(time * 1000));
		} catch (InterruptedException e) {}
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
	public Mouse clickL() {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		hold();
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		return this;
	}
	public Mouse clickL(int x, int y) {
		//System.out.println("Left Click ("+x+", "+y+")");
		return move(x, y).hold().clickL();
	}
	public Mouse clickR() {
		robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		hold();
		robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		return this;
	}
	public Mouse clickR(int x, int y) {
		return move(x,y).hold().clickR();
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
