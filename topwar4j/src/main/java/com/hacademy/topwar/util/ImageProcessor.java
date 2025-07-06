package com.hacademy.topwar.util;

import static org.bytedeco.opencv.global.opencv_core.bitwise_not;
import static org.bytedeco.opencv.global.opencv_core.bitwise_or;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.ADAPTIVE_THRESH_GAUSSIAN_C;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.GaussianBlur;
import static org.bytedeco.opencv.global.opencv_imgproc.fillPoly;
import static org.bytedeco.opencv.global.opencv_imgproc.MORPH_CLOSE;
import static org.bytedeco.opencv.global.opencv_imgproc.MORPH_RECT;
import static org.bytedeco.opencv.global.opencv_imgproc.THRESH_BINARY;
import static org.bytedeco.opencv.global.opencv_imgproc.THRESH_BINARY_INV;
import static org.bytedeco.opencv.global.opencv_imgproc.THRESH_OTSU;
import static org.bytedeco.opencv.global.opencv_imgproc.INTER_CUBIC;
import static org.bytedeco.opencv.global.opencv_imgproc.floodFill;
import static org.bytedeco.opencv.global.opencv_core.CV_32F;
import static org.bytedeco.opencv.global.opencv_core.CV_8U;
import static org.bytedeco.opencv.global.opencv_core.CV_8UC1;
import static org.bytedeco.opencv.global.opencv_imgproc.FILLED;
import static org.bytedeco.opencv.global.opencv_imgproc.adaptiveThreshold;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.dilate;
import static org.bytedeco.opencv.global.opencv_imgproc.getStructuringElement;
import static org.bytedeco.opencv.global.opencv_imgproc.morphologyEx;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;
import static org.bytedeco.opencv.global.opencv_imgproc.threshold;
import static org.bytedeco.opencv.global.opencv_imgproc.filter2D;
import static org.bytedeco.opencv.global.opencv_core.addWeighted;

//core 자료형들
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;

//imgproc 함수
import static org.bytedeco.opencv.global.opencv_imgproc.findContours;
import static org.bytedeco.opencv.global.opencv_imgproc.drawContours;
import static org.bytedeco.opencv.global.opencv_imgproc.contourArea;
import static org.bytedeco.opencv.global.opencv_imgproc.RETR_CCOMP;
import static org.bytedeco.opencv.global.opencv_imgproc.CHAIN_APPROX_SIMPLE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.indexer.FloatIndexer;

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
		threshold(origin, result, 0, 255, THRESH_BINARY_INV + THRESH_OTSU);
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
		resize(origin, result, new Size(origin.cols() * scale, origin.rows() * scale), 0, 0, INTER_CUBIC);
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
		Mat result = new Mat();
		Mat kernel = getStructuringElement(MORPH_RECT, new Size(2, 2));
		morphologyEx(origin, result, MORPH_CLOSE, kernel);
		kernel.release();
		origin.release();
		return result;
	}
	public static Mat adaptiveBinarize(Mat origin) {
		Mat result = new Mat();
		adaptiveThreshold(
				origin, result, 255, 
				ADAPTIVE_THRESH_GAUSSIAN_C, 
				THRESH_BINARY, 11, 2);
		origin.release();
		return result;
	}
	//윤곽선 보강
	public static Mat sharpen(Mat origin) {
		Mat result = new Mat();
		Mat kernel = new Mat(3, 3, CV_32F);
		FloatIndexer ki = kernel.createIndexer();
		ki.put(0, 0, -1); ki.put(0, 1, -1); ki.put(0, 2, -1);
	    ki.put(1, 0, -1); ki.put(1, 1,  9); ki.put(1, 2, -1);
	    ki.put(2, 0, -1); ki.put(2, 1, -1); ki.put(2, 2, -1);
        filter2D(origin, result, origin.depth(), kernel);
		origin.release();
		kernel.release();
		return result;
	}
	public static Mat sharpenMask(Mat origin) {
		//grayscale에 맞게 채널 조정
		Mat origin32f = new Mat();
		origin.convertTo(origin32f, CV_32F);
		
		Mat blurred = new Mat();
		GaussianBlur(origin32f, blurred, new Size(0, 0), 1.5);
		
		Mat result32f = new Mat();
		addWeighted(origin32f, 1.5, blurred, -0.5, 0, result32f);
		
		Mat result = new Mat();
		result32f.convertTo(result, CV_8U);
		
		origin.release();	origin32f.release(); 	blurred.release();	result32f.release();
		return result;
	}
	//통합 전처리
	public static Mat pre(Mat origin) {
		Mat resize = resizeImage(origin, 3);
		Mat gray = grayScale(resize);
		Mat binary = binarize(gray);
		//Mat binary = binarizeOtsu(gray);
		//Mat mask = fillInnerHoles(binary);
		Mat reverse = reverse(binary);
		return reverse;
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
	public static Mat fillInnerHoles(Mat src) {
	    // FloodFill을 위해 마스크 생성 (2px 큰 크기로 필요)
	    int rows = src.rows() + 2;
	    int cols = src.cols() + 2;
	    Mat mask = Mat.zeros(rows, cols, CV_8UC1).asMat();

	    // 외부 배경을 flood fill (검정으로 덮음)
	    floodFill(src, mask, new Point(0, 0), new Scalar(0));
	    
	    // 내부 구멍만 남음 → 나머지와 합쳐서 글자 내부 채우기
	    for (int y = 0; y < src.rows(); y++) {
	        for (int x = 0; x < src.cols(); x++) {
	            byte v = (byte) (src.ptr(y, x).get() & 0xFF);
	            if (v == 0) {                     // 원래 내부 구멍
	                src.ptr(y, x).put((byte) 255);  // 흰색으로 채움
	            } else if (v == 128) {
	                src.ptr(y, x).put((byte) 0);    // 배경은 검정으로
	            }
	        }
	    }

	    return src;
	}
	public static Mat fillContours(Mat binary) {
		// 복사본 생성
        Mat filled = binary.clone();

        // 1. 컨투어 저장용 벡터
        MatVector contours = new MatVector();
        Mat hierarchy = new Mat();

        // 2. 컨투어 추출
        findContours(filled, contours, hierarchy, RETR_CCOMP, CHAIN_APPROX_SIMPLE);

        // 3. 내부를 모두 채우기
        for (long i = 0; i < contours.size(); i++) {
        	//drawContours(filled, contours, (int) i, Scalar.BLACK);//매개변수불일치
        	//fillPoly(filled, new MatVector(contours.get(i)), new Scalar(0, 0, 0, 0));//전부다까매짐
        	Mat contour = contours.get(i);

            // 너무 큰 컨투어 (전체 테두리 등)는 제외
            double area = contourArea(contour);
            if (area < 150) { // ← 이 값은 상황에 따라 조정
                fillPoly(filled, new MatVector(contour), new Scalar(0, 0, 0, 0));
            }
        }

        return filled;
	}
}
