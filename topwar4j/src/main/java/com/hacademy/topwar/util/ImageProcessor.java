package com.hacademy.topwar.util;

import static org.bytedeco.opencv.global.opencv_core.bitwise_not;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.ADAPTIVE_THRESH_GAUSSIAN_C;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.GaussianBlur;
import static org.bytedeco.opencv.global.opencv_imgproc.MORPH_CLOSE;
import static org.bytedeco.opencv.global.opencv_imgproc.MORPH_RECT;
import static org.bytedeco.opencv.global.opencv_imgproc.THRESH_BINARY;
import static org.bytedeco.opencv.global.opencv_imgproc.THRESH_OTSU;
import static org.bytedeco.opencv.global.opencv_imgproc.adaptiveThreshold;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.dilate;
import static org.bytedeco.opencv.global.opencv_imgproc.getStructuringElement;
import static org.bytedeco.opencv.global.opencv_imgproc.morphologyEx;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;
import static org.bytedeco.opencv.global.opencv_imgproc.threshold;

import java.io.File;

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
		threshold(origin, result, 205, 255, THRESH_BINARY);
		origin.release();
		return result;
	}
	public static Mat binarizeOtsu(Mat origin) {
		Mat result = new Mat();
		threshold(origin, result, 0, 255, THRESH_BINARY + THRESH_OTSU);
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
		Mat kernel = getStructuringElement(MORPH_RECT, new Size(2, 2));
		dilate(origin, origin, kernel);
		kernel.release();
		return origin;
	}
	public static Mat morphology(Mat origin) {
		Mat kernel = getStructuringElement(MORPH_RECT, new Size(2, 2));
		morphologyEx(origin, origin, MORPH_CLOSE, kernel);
		kernel.release();
		return origin;
	}
	public static Mat adaptiveBinarize(Mat origin) {
		Mat result = new Mat();
		adaptiveThreshold(origin, result, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, 15, 4);
		origin.release();
		return result;
	}
	//통합 전처리
	public static Mat pre(Mat origin) {
		Mat resize = resizeImage(origin, 3);
		Mat gray = grayScale(resize);
		
		Mat gaussian = gaussianBlur(gray);
		//Mat binary = adaptiveBinarize(gaussian);
		//bitwise_not(binary, binary);//반전
		Mat binary = binarize(gaussian);
		
		Mat morphed = morphology(binary);
		Mat dilate = dilateImage(morphed);
		
//		MatVector contours = new MatVector();
//		findContours(dilate.clone(), contours, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
//		// 5. 윤곽선 내부를 흰색(255)으로 통일
//		for (int i = 0; i < contours.size(); i++) {
//			double area = contourArea(contours.get(i));
//			if (area >= 30 && area <= 500) { // 면적 범위 조절
//				drawContours(dilate, contours, i, new Scalar(255), FILLED, LINE_8, null, 0, new Point());
//			}
//		}
		
		Mat resize2 = resizeImage(dilate, 3);
		return resize2;
	}
	public static Mat reverse(Mat origin) {
		Mat result = new Mat();
		bitwise_not(origin, result);
		origin.release();
		return result;
	}
	public static void save(Mat origin, File target) {
		imwrite(target.getAbsolutePath(), origin);
	}
}
