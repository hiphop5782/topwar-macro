package com.hacademy.topwar.macro.action;

import java.awt.Point;

import com.hacademy.topwar.util.Mouse;

public class MacroMouseAction implements MacroAction {

	private int x, y, wheel;
	private MacroMouseActionType type;
	
	public MacroMouseAction(Point point, MacroMouseActionType type) {
		this(point.x, point.y, type);
	}
	public MacroMouseAction(int x, int y) {
		this(x, y, MacroMouseActionType.CLICK);
	}
	public MacroMouseAction(Point basePoint, int x, int y) {
		this(basePoint.x + x, basePoint.y + y, MacroMouseActionType.CLICK);
	}
	public MacroMouseAction(Point basePoint, int x, int y, MacroMouseActionType type, int wheel) {
		this(basePoint.x + x, basePoint.y + y, type);
		this.wheel = wheel;
	}
	public MacroMouseAction(int x, int y, MacroMouseActionType type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	@Override
	public void doSomething() {
		if(type == MacroMouseActionType.CLICK) {
			Mouse.create().clickL(x, y);
		}
		else if(type == MacroMouseActionType.MOVE) {
			Mouse.create().move(x, y);
		}
		else if(type == MacroMouseActionType.WHEELUP) {
			Mouse.create().move(x, y).wheelUp(wheel);
		}
		else if(type == MacroMouseActionType.WHEELDOWN) {
			Mouse.create().move(x, y).wheelDown(wheel);
		}
	}
	
	@Override
	public String toString() {
		if(type == MacroMouseActionType.CLICK) {
			return "마우스 클릭 ("+x+", "+y+")";
		}
		else if(type == MacroMouseActionType.MOVE) {
			return "마우스 이동 ("+x+", "+y+")";
		}
		else if(type == MacroMouseActionType.WHEELUP) {
			return "마우스 휠 올리기 ("+x+", "+y+") - "+wheel;
		}
		return super.toString();
	}
	
}
