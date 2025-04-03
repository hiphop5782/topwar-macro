package com.hacademy.topwar.constant;

import lombok.Getter;

public enum Delay {
	DARKFORCE1(45), DARKFORCE5(300), 
	WARHAMMER(60), TERROR(60),
	;

	@Getter
	private final int duration;
	Delay(int duration) {
		this.duration = duration;
	}
	
	public long getDurationMillis() {
		return duration * 1000L;
	}
}
