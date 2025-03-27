package com.hacademy.topwar;

import java.awt.MouseInfo;
import java.awt.Point;

import com.hacademy.topwar.util.Mouse;

public class Test16휠이동만 {
	public static void main(String[] args) {
		Point origin = MouseInfo.getPointerInfo().getLocation();
		for(int i=0; i < 125; i++) {
			Mouse.create().move(250, 350).wheelDown(46).hold();
		}
		for(int i=0; i < 125; i++) {
			Mouse.create().move(250, 350).wheelUp(46).hold();
		}
		for(int i=0; i < 16; i++) {
			Mouse.create().move(250, 350).wheelDown(46).hold();
		}
		Mouse.create().move(origin.x, origin.y);
	}
}
