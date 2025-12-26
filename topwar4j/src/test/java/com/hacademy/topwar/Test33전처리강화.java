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
import org.bytedeco.opencv.opencv_core.Scalar;
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
		origin = ImageProcessor.resizeImage(origin, 7);
		ImageUtils.save("ocr/pre/02.png", origin);
		origin = ImageProcessor.addPadding(origin, 10, new Scalar(0));
		ImageUtils.save("ocr/pre/03.png", origin);
		origin = ImageProcessor.grayScale(origin);
		ImageUtils.save("ocr/pre/04.png", origin);
		origin = ImageProcessor.binarize(origin);
		ImageUtils.save("ocr/pre/05.png", origin);
		origin = ImageProcessor.dilateImage(origin, 7);
		ImageUtils.save("ocr/pre/06.png", origin);
		origin = ImageProcessor.reverse(origin);
		ImageUtils.save("ocr/pre/07.png", origin);

		List<String> list = OcrUtils.doOcrDirectory(new File("ocr/pre"));
		DecimalFormat df = new DecimalFormat("00");
		for(int i=0; i < list.size(); i++) {
			String cp = list.get(i);
			System.out.println(df.format(i+1)+".png → "+cp);
		}
	}
}
