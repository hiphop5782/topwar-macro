package com.hacademy.topwar.constant;

import java.awt.Rectangle;

public enum Area {
	RADER(new Rectangle(420, 320, 70, 70));
	
	private Rectangle rect;
	Area(Rectangle rect) {
		this.rect = rect;
	}
	public Rectangle getRect() {
		return rect;
	}
}
