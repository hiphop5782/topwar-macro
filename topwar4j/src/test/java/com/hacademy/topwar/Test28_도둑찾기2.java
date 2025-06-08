package com.hacademy.topwar;

import java.awt.image.BufferedImage;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;

import com.hacademy.topwar.util.ImageUtils;

public class Test28_도둑찾기2 {
	public static void main(String[] args) throws Exception {
		BufferedImage bm = ImageUtils.load("images/search/robber.png");
		Mat target = ImageUtils.bufferedImageToMat(bm);
		BufferedImage bm2 = ImageUtils.load("images/search/map.png");
		Mat source = ImageUtils.bufferedImageToMat(bm2);
		Point match = ImageUtils.matchBinaryTemplate(source, target);
		if (match != null) {
		    System.out.println("탐지 성공! 위치: " + match.x() + ", " + match.y());
		} else {
		    System.out.println("탐지 실패");
		}
	}
}
