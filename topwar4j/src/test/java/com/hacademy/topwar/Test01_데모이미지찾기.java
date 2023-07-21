package com.hacademy.topwar;

import static org.bytedeco.opencv.global.opencv_core.CV_32FC1;
import static org.bytedeco.opencv.global.opencv_core.CV_8UC1;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.TM_CCOEFF_NORMED;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.matchTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.opencv.opencv_calib3d.*;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_highgui.*;
import org.bytedeco.opencv.opencv_imgproc.*;
import org.bytedeco.opencv.opencv_objdetect.*;
import static org.bytedeco.opencv.global.opencv_calib3d.*;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_highgui.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_objdetect.*;

public class Test01_데모이미지찾기 {
	public static void main(String[] args) {
		String path = System.getProperty("user.dir");
		Mat base = imread(path + "\\images\\demo\\base.png");
		Mat baseGray = new Mat(base.size(), CV_8UC1);
		cvtColor(base, baseGray, COLOR_BGR2GRAY);

		Mat find = imread(path + "\\images\\demo\\find.png");
		Mat findGray = new Mat(find.size(), CV_8UC1);
		cvtColor(find, findGray, COLOR_BGR2GRAY);

		Size size = new Size(baseGray.cols() - findGray.cols() + 1, baseGray.rows() - findGray.rows() + 1);
		Mat result = new Mat(size, CV_32FC1);
		matchTemplate(baseGray, findGray, result, TM_CCOEFF_NORMED);

		List<Point> list = getPointsFromMatAboveThreshold(result, 0.90f);
		System.out.println(list.size());
		for(Point point : list) {
			Rect rect = new Rect(point.x(), point.y(), 100, 100);
			rectangle(base, rect, randColor(), 2, 0, 0);
		}

//		DoublePointer minVal = new DoublePointer();
//		DoublePointer maxVal = new DoublePointer();
//		Point min = new Point();
//		Point max = new Point();
//		minMaxLoc(result, minVal, maxVal, min, max, null);
//		
//		Rect rect = new Rect(max.x(), max.y(), findGray.cols(), findGray.rows());
//		System.out.println(rect.x()+", "+rect.y()+", "+rect.width()+", "+rect.height());
//		rectangle(base, rect, randColor(), 2, 0, 0);

		imshow("Original", base);
		waitKey(0);
		destroyAllWindows();
	}

	// some usefull things.
	public static Scalar randColor() {
		int b, g, r;
		b = ThreadLocalRandom.current().nextInt(0, 255 + 1);
		g = ThreadLocalRandom.current().nextInt(0, 255 + 1);
		r = ThreadLocalRandom.current().nextInt(0, 255 + 1);
		return new Scalar(b, g, r, 0);
	}

	public static List<Point> getPointsFromMatAboveThreshold(Mat m, float t) {
		List<Point> matches = new ArrayList<Point>();
		FloatIndexer indexer = m.createIndexer();
		for (int y = 0; y < m.rows(); y++) {
			for (int x = 0; x < m.cols(); x++) {
				if (indexer.get(y, x) > t) {
					System.out.println("(" + x + "," + y + ") = " + indexer.get(y, x));
					matches.add(new Point(x, y));
				}
			}
		}
		return matches;
	}

}
