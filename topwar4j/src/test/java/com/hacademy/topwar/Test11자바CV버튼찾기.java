package com.hacademy.topwar;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import com.hacademy.topwar.constant.Button;
import com.hacademy.topwar.util.ImageUtils;
import com.hacademy.topwar.util.Mouse;

public class Test11자바CV버튼찾기 {
	public static void main(String[] args) throws Exception {
		long begin = System.currentTimeMillis();
		Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//		Rectangle rect = new Rectangle(0, 0, 500, 700);
		//Point p = ImageUtils.searchButton(rect, Button.HERO);
		Point p = ImageUtils.searchRaderButton(rect);
		long end = System.currentTimeMillis();
		
		System.out.println(p);
		
		Mouse.create().clickL(p.x, p.y);
		
		long duration = end - begin;
		System.out.println("소요시간 : " + duration + "ms");
	}
}
