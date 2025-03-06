package com.hacademy.topwar.macro.action;

import java.awt.Point;

import com.hacademy.topwar.util.Mouse;

import lombok.Data;

@Data
public class MacroMouseAction implements MacroAction {

	private int x, y, wheel, tx, ty;
	private MacroMouseActionType type;
	private boolean single;
	private long singleDelay = 500L;
	
	public MacroMouseAction(int x, int y, MacroMouseActionType type, boolean single) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.single = single;
	}
	public MacroMouseAction(int x, int y, MacroMouseActionType type) {
		this(x, y, type, false);
	}
	public MacroMouseAction(Point point, MacroMouseActionType type) {
		this(point.x, point.y, type);
	}
	public MacroMouseAction(int x, int y) {
		this(x, y, MacroMouseActionType.CLICK);
	}
	public MacroMouseAction(Point basePoint, int x, int y) {
		this(basePoint.x + x, basePoint.y + y, MacroMouseActionType.CLICK);
	}
	public MacroMouseAction(Point basePoint, int x, int y, boolean single) {
		this(basePoint.x + x, basePoint.y + y, MacroMouseActionType.CLICK, single);
	}
	public MacroMouseAction(Point basePoint, int x, int y, MacroMouseActionType type, int wheel) {
		this(basePoint.x + x, basePoint.y + y, type);
		this.wheel = wheel;
	}
	public MacroMouseAction(Point basePoint, int x, int y, MacroMouseActionType type, int wheel, boolean single) {
		this(basePoint, x, y, type, wheel);
		this.single = single;
	}
	public MacroMouseAction(Point basePoint, int x, int y, int tx, int ty, MacroMouseActionType type) {
		this(basePoint.x + x, basePoint.y + y, type);
		this.tx = basePoint.x + tx;
		this.ty = basePoint.y + ty;
		this.type = type;
	}
	public MacroMouseAction(Point basePoint, int x, int y, int tx, int ty, MacroMouseActionType type, boolean single) {
		this(basePoint, x, y, tx, ty, type);
		this.single = single;
	}
	
	@Override
	public void doSomething() {
		Mouse mouse = switch(type) {
		case CLICK				-> Mouse.create().clickL(x, y);
		case MOVE				-> Mouse.create().move(x, y);
		case WHEELUP			-> Mouse.create().move(x, y).wheelUp(wheel);
		case WHEELDOWN			-> Mouse.create().move(x, y).wheelDown(wheel);
		case DRAG				-> Mouse.create().dragL(x, y, tx, ty, 1d);
		default					-> Mouse.create();
		};
		if(single) {
			mouse.hold(singleDelay/1000f);
		}
	}
	
	@Override
	public String toString() {
		return switch(type) {
		case CLICK				-> "마우스 클릭 ("+x+", "+y+")";
		case MOVE				-> "마우스 이동 ("+x+", "+y+")";
		case WHEELUP			-> "마우스 휠 올리기 ("+x+", "+y+") - "+wheel;
		case WHEELDOWN			-> "마우스 휠 내리기 ("+x+", "+y+") - "+wheel;
		case DRAG				-> "마우스 드래그 ("+x+","+y+") → ("+tx+","+ty+")";
		default					-> super.toString();
		};
	}
	@Override
	public long getDuration() {
		long duration = switch(type) {
		case CLICK->500L;
		case MOVE->250L;
		case WHEELUP, WHEELDOWN->750L;
		case DRAG->1200L;
		default->1000L;
		};
		
		if(single) {
			duration += 500;
		}
		return duration;
	}
	
}
