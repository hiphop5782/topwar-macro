package com.hacademy.topwar.util;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.Arrays;

public class MonitorUtils {
	public static GraphicsEnvironment env() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment();
	}
	public static GraphicsDevice main() {
		return env().getDefaultScreenDevice();
	}
	public static GraphicsConfiguration mainConfig() {
		return main().getDefaultConfiguration();
	}
	public static Rectangle getMainMonitorBounds() {
		return mainConfig().getBounds();
	}
	public static GraphicsDevice[] all() {
		return env().getScreenDevices();
	}
	public static GraphicsDevice findDevice(int monitor) {
		return all()[monitor];
	}
	public static GraphicsConfiguration findConfig(int monitor) {
		return findDevice(monitor).getDefaultConfiguration();
	}
	public static Rectangle getMonitorBounds(int monitor) {
		return findConfig(monitor).getBounds();
	}
}
