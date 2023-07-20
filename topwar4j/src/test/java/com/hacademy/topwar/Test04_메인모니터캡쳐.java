package com.hacademy.topwar;

import static org.bytedeco.opencv.global.opencv_highgui.destroyAllWindows;
import static org.bytedeco.opencv.global.opencv_highgui.imshow;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;

import java.awt.Rectangle;
import java.io.IOException;

import org.bytedeco.opencv.opencv_core.Mat;

import com.hacademy.topwar.util.CaptureUtils;
import com.hacademy.topwar.util.MonitorUtils;

public class Test04_메인모니터캡쳐 {
	public static void main(String[] args) throws IOException {
		Rectangle rect = MonitorUtils.getMainMonitorBounds();
		CaptureUtils.saveTempBfi(rect);
		Mat mat = CaptureUtils.captureMat(rect);
		imshow("mat", mat);
		waitKey(0);
		destroyAllWindows();
	}
}
