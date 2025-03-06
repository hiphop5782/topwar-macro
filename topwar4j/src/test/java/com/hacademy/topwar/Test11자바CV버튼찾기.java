package com.hacademy.topwar;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import com.hacademy.topwar.constant.Button;
import com.hacademy.topwar.util.ImageUtils;

public class Test11자바CV버튼찾기 {
	public static void main(String[] args) throws Exception {
		long begin = System.currentTimeMillis();
		Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		Point p = ImageUtils.searchButton(rect, Button.HERO);
		long end = System.currentTimeMillis();
		
		System.out.println(p);
		
		long duration = end - begin;
		System.out.println("소요시간 : " + duration + "ms");
	}
}
