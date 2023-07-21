package com.hacademy.topwar.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;

public class ImageUtils {
	
	public static final float THRESHOLD = 0.9f;
	
	public static Point findImage(Mat origin, Mat find) {
		return findImage(origin, find, THRESHOLD);
	}
	public static Point findImage(Mat origin, Mat find, float threshold) {
		List<Point> points = findImageList(origin, find, threshold);
		if(points == null || points.size() == 0) return null;
		return points.get(0);
	}
	
	public static List<Point> findImageList(Mat origin, Mat find) {
		return findImageList(origin, find, THRESHOLD);
	}
	public static List<Point> findImageList(Mat origin, Mat find, float threshold) {
		int rows = origin.rows() - find.rows() + 1;
		int cols = origin.cols() - find.cols() + 1;
		Size size = new Size(cols, rows);
		Mat result = new Mat(size, opencv_core.CV_32FC1);
		opencv_imgproc.matchTemplate(origin, find, result, opencv_imgproc.TM_CCOEFF_NORMED);
		return getPointsFromMatAboveThreshold(result, threshold);
	}
	
	/**
	 * 임계치(threshold)에 따른 유사지점을 찾아 좌표로 반환하는 메소드
	 * @param m - 비교 결과 Mat 데이터
	 * @param t - 임계치(threshold)
	 * @return Point list of find images
	 */
	public static List<Point> getPointsFromMatAboveThreshold(Mat m, float t) {
		List<Point> matches = new ArrayList<Point>();
		FloatIndexer indexer = m.createIndexer();
		for (int y = 0; y < m.rows(); y++) {
			for (int x = 0; x < m.cols(); x++) {
				if (indexer.get(y, x) > t) {
//					System.out.println("(" + x + "," + y + ") = " + indexer.get(y, x));
					matches.add(new Point(x, y));
				}
			}
		}
		return matches;
	}
	
	/**
	 * imread를 대신 처리하는 함수
	 * @param path 이미지 파일의 경로(프로젝트 기준)
	 * @return 로드된 Mat 데이터
	 */
	public static Mat load(String path) {
		switch(path.charAt(0)) {
		case '\\': case '/':
		default:
			path = File.separator + path;
		}
		String base = System.getProperty("user.dir");
		return opencv_imgcodecs.imread(base+path);
	}
	
	/**
	 * 이미지 임시 출력 메소드
	 * @param mat - 출력할 데이터
	 */
	public static void display(Mat mat) {
		opencv_highgui.imshow("display", mat);
		opencv_highgui.waitKey(0);
		opencv_highgui.destroyAllWindows();
	}
	
	/**
	 * Generate Random Color
	 * @return random color Scalar Object
	 */
	public static Scalar randColor() {
		int b, g, r;
		b = ThreadLocalRandom.current().nextInt(0, 255 + 1);
		g = ThreadLocalRandom.current().nextInt(0, 255 + 1);
		r = ThreadLocalRandom.current().nextInt(0, 255 + 1);
		return new Scalar(b, g, r, 0);
	}
}
