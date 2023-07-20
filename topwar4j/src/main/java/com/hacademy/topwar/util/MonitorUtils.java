package com.hacademy.topwar.util;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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
}
