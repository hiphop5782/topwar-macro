package com.hacademy.topwar.util;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;

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
	public static Rectangle getCurrentMonitorBounds() {
		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
		for(GraphicsDevice device : all()) {
			Rectangle rect = device.getDefaultConfiguration().getBounds();
			if(rect.contains(mousePoint)) {
				return rect;
			}
		}
		return null;
	}
	public static Rectangle getCurrentMonitorCenterBounds(int width, int height) {
		Rectangle rect = getCurrentMonitorBounds();
		int x = rect.x + (rect.width / 2 - width / 2);
		int y = rect.y + (rect.height / 2 - height / 2);
		return new Rectangle(x, y, width, height);
	}
	public static Rectangle getAppLocatedMonitorBounds(int x, int y) {
		Point mousePoint = new Point(x, y);
		for(GraphicsDevice device : all()) {
			Rectangle rect = device.getDefaultConfiguration().getBounds();
			if(rect.contains(mousePoint)) {
				return rect;
			}
		}
		return null;
	}
	public static Rectangle getAppLocatedMonitorCenterBounds(int x, int y, int width, int height) {
		Rectangle rect = getAppLocatedMonitorBounds(x, y);
		int px = rect.x + (rect.width / 2 - width / 2);
		int py = rect.y + (rect.height / 2 - height / 2);
		return new Rectangle(px, py, width, height);
	}
}
