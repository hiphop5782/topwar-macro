package com.hacademy.topwar.util;

import org.bytedeco.opencv.opencv_core.Mat;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

//이미지 전처리 도구
public class ImageProcessor {
	//회색조 변환
	public static Mat grayScale(Mat origin) {
		Mat result = new Mat();
		cvtColor(origin, result, COLOR_BGR2GRAY);
		origin.release();
		return result;
	}
	//이진화
	public static Mat binarize(Mat origin) {
		Mat result = new Mat();
		threshold(origin, result, 100, 255, THRESH_BINARY_INV);
		origin.release();
		return result;
	}
}
