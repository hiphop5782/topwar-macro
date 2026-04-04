package com.hacademy.topwar;

import java.awt.Rectangle;
import java.io.IOException;

import com.hacademy.topwar.util.Mouse;
import com.hacademy.topwar.util.RectUtils;
import com.hacademy.topwar.util.js.JavascriptUtil;

public class Test38오류테스트 {
	public static void main(String[] args) throws IOException {
		Rectangle rect = RectUtils.getRect(true);
		JavascriptUtil.moveToServer(rect, 4349);
		Mouse.create()
		.clickL(rect.x + 243, rect.y + 355).hold(1f)
		.clickL(rect.x + 299, rect.y + 657).hold(1f);
	}
}
