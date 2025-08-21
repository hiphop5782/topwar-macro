package com.hacademy.topwar;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.List;

import javax.imageio.ImageIO;
import javax.naming.ServiceUnavailableException;

import org.bytedeco.opencv.opencv_core.Mat;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;

import com.hacademy.topwar.util.ImageProcessor;
import com.hacademy.topwar.util.ImageUtils;
import com.hacademy.topwar.util.OcrUtils;

public class Test33전처리강화 {
	public static void main(String[] args) throws AbortedByHookException, ConcurrentRefUpdateException, NoHeadException, NoMessageException, ServiceUnavailableException, UnmergedPathsException, WrongRepositoryStateException, IOException, GitAPIException, URISyntaxException {
		BufferedImage bm = ImageIO.read(new File(System.getProperty("user.dir"), "ocr/dummy.png"));
		Mat origin = ImageUtils.bufferedImageToMat(bm);
		ImageUtils.save("ocr/pre/01.png", origin);
		Mat resize = ImageProcessor.resizeImage(origin, 3);
		ImageUtils.save("ocr/pre/02.png", resize);
		Mat gray = ImageProcessor.grayScale(resize);
        ImageUtils.save("ocr/pre/03.png", gray);
//     기존
        Mat bin = ImageProcessor.binarize(gray);
        ImageUtils.save("ocr/pre/04.png", bin);
//     기존+@
        Mat dilate = ImageProcessor.dilateImage(bin, 4);
        ImageUtils.save("ocr/pre/05.png", dilate);
        Mat reverse = ImageProcessor.reverse(dilate);
		ImageUtils.save("ocr/pre/06.png", reverse);
        
//        Mat convert = ImageProcessor.findAndFill(gray, 200);
////        Mat convert = ImageProcessor.findAndFillPixel(gray);
//        ImageUtils.save("ocr/pre/04.png", convert);
//        Mat blur = ImageProcessor.gaussianBlur(convert, 5);
//        ImageUtils.save("ocr/pre/05.png", blur);
////		Mat morph = ImageProcessor.morphology(adapt);
//		Mat morph = ImageProcessor.morphologyOpenAndClose(blur);
//		ImageUtils.save("ocr/pre/06.png", morph);
//		Mat convert2 = ImageProcessor.findAndFillPixel(morph, 110);
//		ImageUtils.save("ocr/pre/07.png", convert2);
//		Mat contour = ImageProcessor.fillContours(convert2);
//		ImageUtils.save("ocr/pre/08.png", contour);
//		Mat adapt = ImageProcessor.adaptiveBinarize(contour);
//		ImageUtils.save("ocr/pre/09.png", adapt);
////		Mat binary = ImageProcessor.binarize(convert, 100);
////		ImageUtils.save("ocr/pre-06.png", binary);
////		Mat binary = ImageProcessor.binarizeOtsu(convert);
//		//Mat binary = ImageProcessor.binarizeOtsu(gray);
//		//Mat mask = ImageProcessor.fillInnerHoles(binary);

		List<String> list = OcrUtils.doOcrDirectory(new File("ocr/pre"));
		DecimalFormat df = new DecimalFormat("00");
		for(int i=0; i < list.size(); i++) {
			String cp = list.get(i);
			System.out.println(df.format(i+1)+".png → "+cp);
		}
	}
}
