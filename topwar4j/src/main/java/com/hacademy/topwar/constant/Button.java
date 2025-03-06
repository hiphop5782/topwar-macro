package com.hacademy.topwar.constant;

import lombok.Getter;

public enum Button {
	RADER("rader"),
	HERO("hero"),
	ALLIANCE("alliance"),
	POST("post"),
	INVENTORY("inventory"),
	;
	
	@Getter
	private final String type;
	Button(String type) {
		this.type = type;
	}
}
