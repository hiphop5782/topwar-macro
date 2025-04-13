package com.hacademy.topwar.util;

import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.GaussianBlur;
import static org.bytedeco.opencv.global.opencv_imgproc.MORPH_RECT;
import static org.bytedeco.opencv.global.opencv_imgproc.THRESH_BINARY;
import static org.bytedeco.opencv.global.opencv_imgproc.THRESH_OTSU;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.getStructuringElement;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;
import static org.bytedeco.opencv.global.opencv_imgproc.threshold;
import static org.bytedeco.opencv.global.opencv_imgproc.dilate;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;

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
		threshold(origin, result, 0, 255, THRESH_BINARY | THRESH_OTSU);
		origin.release();
		return result;
	}
	public static Mat gaussianBlur(Mat origin) {
		Mat result = new Mat();
		GaussianBlur(origin, result, new Size(3, 3), 0);
		origin.release();
		return result;
	}
	public static Mat resizeImage(Mat origin, int scale) {
		Mat result = new Mat();
		resize(origin, result, new Size(origin.cols() * scale, origin.rows() * scale));
		origin.release();
		return result;
	}
	public static Mat dilateImage(Mat origin) {
		Mat result = getStructuringElement(MORPH_RECT, new Size(2, 2));
		dilate(origin, origin, result);
		return origin;
	}
	//통합 전처리
	public static Mat pre(Mat origin) {
		Mat resize = resizeImage(origin, 3);
		Mat gray = grayScale(resize);
		Mat gaussian = gaussianBlur(gray);
		Mat binary = binarize(gaussian);
		Mat dilate = dilateImage(binary);
		Mat resize2 = resizeImage(dilate, 3);
		return resize2;
	}
}
