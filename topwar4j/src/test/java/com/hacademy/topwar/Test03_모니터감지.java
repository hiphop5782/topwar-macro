package com.hacademy.topwar;

import java.awt.DisplayMode;
import java.awt.GraphicsConfigTemplate;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

public class Test03_모니터감지 {
	public static void main(String[] args) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice mainDevice = env.getDefaultScreenDevice();
		DisplayMode mainMode = mainDevice.getDisplayMode();
		System.out.println("메인모니터");
		System.out.println(mainDevice.getIDstring());
		System.out.println(mainMode);
		System.out.println(mainDevice.getDefaultConfiguration().getBounds());
		//System.out.println("폭 = " + mainMode.getWidth()+", 높이 = "+ mainMode.getHeight());
		
		GraphicsDevice[] devices = env.getScreenDevices();
		System.out.println("devices = " + devices.length);
		for(GraphicsDevice device : devices) {
			System.out.println(device.getIDstring());
			GraphicsConfiguration config = device.getDefaultConfiguration();
			System.out.println(config.getBounds());
		}
	}
}
