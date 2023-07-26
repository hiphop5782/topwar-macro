package com.hacademy.topwar;

import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;

import com.hacademy.topwar.util.CaptureUtils;
import com.hacademy.topwar.util.ImageUtils;
import com.hacademy.topwar.util.MonitorUtils;

public class Test05_모니터에서데모이미지찾기 {
	public static void main(String[] args) throws IOException {
		//메인모니터를 캡쳐해서 find.png 찾기
		Mat monitor = CaptureUtils.captureMatGrayscale(MonitorUtils.getMainMonitorBounds());
		Mat find = ImageUtils.load("/images/button/search.png");
		Point point = ImageUtils.findImage(monitor, find);
		Rect r = new Rect(point.x(), point.y(), 80, 80);
		rectangle(monitor, r, ImageUtils.randColor(), 2, 0, 0);
		ImageUtils.display(monitor);
	}
}
