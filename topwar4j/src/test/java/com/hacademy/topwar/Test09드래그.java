package com.hacademy.topwar;

import com.hacademy.topwar.util.Mouse;

public class Test09드래그 {
	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(5000L);
		Mouse.create().dragL(300, 700, 300, 400, 1d);
	}
}
