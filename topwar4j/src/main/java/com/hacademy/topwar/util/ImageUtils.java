package com.hacademy.topwar.util;

import static org.bytedeco.opencv.global.opencv_core.CV_32FC1;
import static org.bytedeco.opencv.global.opencv_core.CV_8UC1;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.TM_CCOEFF_NORMED;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.matchTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;

public class ImageUtils {
	
	public static final float THRESHOLD = 0.9f;
	
	private static Map<String, Mat> images = Collections.synchronizedMap(new HashMap<>());
	static {
		File dir = new File(System.getProperty("user.dir"), "images");
		loadImages(dir);
	}
	private static void loadImages(File file) {
		if(file.isFile()) {
			String abs = file.getAbsolutePath();
			images.put(abs, load(abs));
		}
		else if(file.isDirectory()){
			File[] list = file.listFiles();
			if(list != null) {
				for(File f : list) {
					loadImages(f);
				}
			}
		}
	}
	
	public static Point findImage(Mat origin, Mat find) {
		Size size = new Size(origin.cols() - find.cols() + 1, origin.rows() - find.rows() + 1);
		Mat result = new Mat(size, CV_32FC1);
		matchTemplate(origin, find, result, TM_CCOEFF_NORMED);
		DoublePointer minVal = new DoublePointer();
		DoublePointer maxVal = new DoublePointer();
		Point min = new Point();
		Point max = new Point();
		opencv_core.minMaxLoc(result, minVal, maxVal, min, max, null);
		Rect rect = new Rect(max.x(), max.y(), find.cols(), find.rows());
		System.out.println(rect.x()+", "+rect.y()+", "+rect.width()+", "+rect.height());
		return new Point(rect.x() + rect.width()/2, rect.y()+rect.height()/2);
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
		String base = System.getProperty("user.dir");
		String key = path.startsWith(base) ? path : base+path;
		if(images.containsKey(key)) {
			return images.get(key);
		}
		Mat origin = opencv_imgcodecs.imread(key);
		Mat grayscale = new Mat(origin.size(), CV_8UC1);
		cvtColor(origin, grayscale, COLOR_BGR2GRAY);
		return grayscale;
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
	
	/**
	 * Image resize
	 */
	public static Mat resize(Mat origin, int rate) {
		Size size = origin.size();
		Mat dest = new Mat(size.height()*rate/100, size.width()*rate/100, origin.type());
		opencv_imgproc.resize(origin, dest, dest.size(), 0, 0, opencv_imgproc.INTER_CUBIC);
		return dest;
	}
}
