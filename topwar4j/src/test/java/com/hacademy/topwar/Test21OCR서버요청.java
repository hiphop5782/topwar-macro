package com.hacademy.topwar;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.opencv.opencv_core.Mat;

import com.hacademy.topwar.util.ImageProcessor;
import com.hacademy.topwar.util.ImageUtils;
import com.hacademy.topwar.util.OcrUtils;

public class Test21OCR서버요청 {
	public static void main(String[] args) throws IOException, InterruptedException {
		File target = new File("C:/Users/user1/desktop", "dummy.png");
		if(target.exists() == false) return;
		BufferedImage image = ImageIO.read(target);
		Mat src = ImageUtils.bufferedImageToMat(image);
		Mat gray = ImageProcessor.grayScale(src);
		Mat binary = ImageProcessor.binarizeOtsu(gray);
		Mat reverse = ImageProcessor.reverse(binary);
		Mat resize = ImageProcessor.resizeImage(reverse, 5);
		File target2 = new File("C:/Users/user1/desktop", "dummy2.png");
		ImageProcessor.save(resize, target2);
		String result = OcrUtils.doOcrFile(target2.toPath());
		System.out.println("result = " + result);
		
//		File dir = new File("C:\\Users\\user1\\git\\topwar-macro\\topwar4j\\ocr\\kartz");
//		if(dir.exists() == false) return;
//		List<String> list = OcrUtils.doOcrDirectory(dir);
//		for(String result : list) {
//			System.out.println("result = " + result);
//		}
	}
}
