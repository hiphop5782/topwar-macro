package com.hacademy.topwar.constant;

import java.awt.image.BufferedImage;

import com.hacademy.topwar.util.ImageUtils;

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
	@Getter
	private BufferedImage image;
	Button(String type) {
		this.type = type;
		try {
			this.image = ImageUtils.loadButton(type);
		}
		catch(Exception e) {}
	}
}
