package com.hacademy.topwar;

import java.io.IOException;

import com.hacademy.topwar.util.Mouse;

public class Test06__마우스클릭 {
	public static void main(String[] args) throws IOException {
		Mouse.create()
			.clickImgL("/images/button/search.png")
//			.hold(5)
//			.clickImgL("/images/button/search2.png")
//			.hold(5)
//			.clickImgL("/images/enemy/target.png")
//			.hold(5)
//			.clickImgL("/images/button/attack+5.png")
			;
	}
}
