package com.hacademy.topwar.util;

import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_ANYCOLOR;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_ANYDEPTH;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imdecode;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.opencv_core.Mat;

public class CaptureUtils {
	private static Robot robot;
	static {
		try {
			robot = new Robot();
		}
		catch(AWTException e) {
			System.err.println(e.getMessage());
		}
	}
	public static BufferedImage captureBfi(Rectangle rect) {
		return robot.createScreenCapture(rect);
	}
	public static void saveTempBfi(Rectangle rect) throws IOException {
		String home = System.getProperty("user.dir");
		ImageIO.write(captureBfi(rect), "png", new File(home, "images/temp/capture.png"));
	}
	public static Mat captureMat(Rectangle rect) throws IOException {
		return bfi2mat(captureBfi(rect));
	}
	public static Mat bfi2mat(BufferedImage bfi) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    ImageIO.write(bfi, "jpg", byteArrayOutputStream);
	    byte[] data = byteArrayOutputStream.toByteArray();
	    Mat mat = imdecode(new Mat(new BytePointer(data)), IMREAD_ANYDEPTH | IMREAD_ANYCOLOR);
	    return mat;
	}
}
