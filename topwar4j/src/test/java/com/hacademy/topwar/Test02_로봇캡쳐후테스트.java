package com.hacademy.topwar;

import static org.bytedeco.opencv.global.opencv_highgui.destroyAllWindows;
import static org.bytedeco.opencv.global.opencv_highgui.imshow;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.opencv.opencv_core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.MatOfByte;

public class Test02_로봇캡쳐후테스트 {
	public static void main(String[] args) throws AWTException, IOException {
		Robot robot = new Robot();
		Rectangle rect = new Rectangle(0, 0, 1920, 1080);
		BufferedImage image = robot.createScreenCapture(rect);
		Mat base = bufferedImageToMat(image);
		imshow("base", base);
		waitKey(0);
		destroyAllWindows();
	}
	public static Mat bufferedImageToMat(BufferedImage image) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    ImageIO.write(image, "jpg", byteArrayOutputStream);
	    byte[] data = byteArrayOutputStream.toByteArray();
	    //Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
	    Mat mat = new Mat(data, true);
	    return mat;
	}
}
