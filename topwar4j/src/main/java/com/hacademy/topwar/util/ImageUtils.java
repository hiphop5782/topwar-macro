package com.hacademy.topwar.util;

import static org.bytedeco.opencv.global.opencv_core.CV_8UC3;
import static org.bytedeco.opencv.global.opencv_core.minMaxLoc;
import static org.bytedeco.opencv.global.opencv_imgproc.TM_CCOEFF_NORMED;
import static org.bytedeco.opencv.global.opencv_imgproc.matchTemplate;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Size;

import com.hacademy.topwar.Topwar4jApplication;
import com.hacademy.topwar.constant.Button;

import lombok.Getter;

/**
 * 이미지 비교 유틸
 */
public class ImageUtils {

	// root directory
	private static File dir;
	static {
		try {
			dir = new File(Topwar4jApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		}
		catch(Exception e) {}
	}
	@Getter
	private static final File images = new File(dir, "images");
	private static final File buttons = new File(images, "button");
	
	private static final double SCALE_MIN = 0.8;
	private static final double SCALE_MAX = 1.2;
	private static final double SCALE_STEP = 0.1;
	private static final double THRESHOLD = 0.8;
	
	// ✅ 지정한 영역에서 버튼 찾기
	public static java.awt.Point searchButton(Rectangle screenRect, Button button) throws Exception {
		if(button.getImage() == null) throw new Exception("["+button.getType()+"] 이미지 없음");
		return searchImage(screenRect, button.getImage());
	}
	// ✅ 지정한 영역에서 이미지 찾기
	public static java.awt.Point searchImage(Rectangle screenRect, BufferedImage buf) throws Exception {
		Mat areaMat = captureScreenToMat(screenRect);
		Mat buttonMat = bufferedImageToMat(buf);
		Mat resultMat = new Mat();
		
		//탐지
		Point match = findImageMultiScale(areaMat, buttonMat, resultMat);
		if(match== null) return null;
		
		//중심좌표 계산
		int x = match.x() + resultMat.cols()/2;
		int y = match.y() + resultMat.rows()/2;
		
		//메모리 정리
		areaMat.release();
		buttonMat.release();
		resultMat.release();
		
		return new java.awt.Point(x, y);
	}
	public static java.awt.Point searchRaderButton(Rectangle baseRect) throws Exception {
		return searchButton(baseRect, Button.RADER);
	}
	
	// ✅ 지정한 영역 캡쳐
	public static Mat captureScreenToMat(java.awt.Point point, Dimension dimension) throws Exception {
		return captureScreenToMat(new Rectangle(point, dimension));
	}
	public static Mat captureScreenToMat(Rectangle rectangle) throws Exception {
		BufferedImage im = captureScreen(rectangle);
		return bufferedImageToMat(im);
	}
	public static BufferedImage captureScreen(java.awt.Point point, Dimension dimension) throws Exception {
		Rectangle rectangle = new Rectangle(point, dimension);
		return captureScreen(rectangle);
	}
	public static BufferedImage captureScreen(Rectangle rectangle) throws Exception {
		Robot robot = new Robot();
		return robot.createScreenCapture(rectangle);
	}
	
	// ✅ 지정한 이미지 로드
	public static BufferedImage loadButton(Button button) throws FileNotFoundException, IOException {
		File target = new File(buttons, button.getType()+".png");
		return ImageIO.read(new FileInputStream(target));
	}
	public static Mat loadButtonToMat(Button button) throws FileNotFoundException, IOException {
		BufferedImage im = loadButton(button);
		return bufferedImageToMat(im);
	}
	public static BufferedImage loadButton(String buttonType) throws FileNotFoundException, IOException {
		File target = new File(buttons, buttonType+".png");
		return ImageIO.read(new FileInputStream(target));
	}
	public static Mat loadButtonToMat(String buttonType) throws FileNotFoundException, IOException {
		BufferedImage im = loadButton(buttonType);
		return bufferedImageToMat(im);
	}

	// ✅ BufferedImage → JavaCV Mat 변환 (파일 없이 비교 가능)
	public static Mat bufferedImageToMat(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		Mat mat = new Mat(height, width, CV_8UC3);
		ByteBuffer buffer = ByteBuffer.allocate(width * height * 3);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = image.getRGB(x, y);
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // G
				buffer.put((byte) (pixel & 0xFF)); // B
			}
		}
		mat.data().put(buffer.array());
		return mat;
	}

	// ✅ JavaCV 템플릿 매칭 (이미지를 파일 없이 비교)
	public static Point findImage(Mat screen, Mat search) {
		Mat result = new Mat();
		matchTemplate(screen, search, result, TM_CCOEFF_NORMED);

		// 최대 일치 좌표 찾기
		DoublePointer minVal = new DoublePointer(1);
		DoublePointer maxVal = new DoublePointer(1);
		Point minLoc = new Point();
		Point maxLoc = new Point();
		minMaxLoc(result, minVal, maxVal, minLoc, maxLoc, null);
		if (maxVal.get() > THRESHOLD) { // 유사도 80% 이상만 허용
			return new Point((int) maxLoc.x(), maxLoc.y());
		}
		return null;
	}

	// ✅ 크기가 다른 이미지도 매칭할 수 있도록 여러 크기로 비교
	public static Point findImageMultiScale(Mat screen, Mat search, Mat result) {
		return findImageMultiScale(screen, search, result, SCALE_MIN, SCALE_MAX, SCALE_STEP);
	}
	public static Point findImageMultiScale(Mat screen, Mat search, Mat result, double minScale, double maxScale,
			double scaleStep) {
		Point bestLoc = null;
		double bestMatch = 0;

		for (double scale = minScale; scale <= maxScale; scale += scaleStep) {
			// 🔹 이미지 크기 조절
			Mat resizedSearch = new Mat();
			resize(search, resizedSearch, new Size((int) (search.cols() * scale), (int) (search.rows() * scale)));

			if (resizedSearch.cols() > screen.cols() || resizedSearch.rows() > screen.rows()) {
				resizedSearch.release();
				continue;
			}

			// 🔹 템플릿 매칭 수행
			matchTemplate(screen, resizedSearch, result, TM_CCOEFF_NORMED);

			// 🔹 최대 일치 좌표 찾기
			DoublePointer minVal = new DoublePointer(1);
			DoublePointer maxVal = new DoublePointer(1);
			Point minLoc = new Point();
			Point maxLoc = new Point();

			minMaxLoc(result, minVal, maxVal, minLoc, maxLoc, null);

			// 🔹 최적의 일치율 업데이트
			if (maxVal.get() > bestMatch) {
				bestMatch = maxVal.get();
				bestLoc = new Point(maxLoc.x(), maxLoc.y());
			}

			resizedSearch.release();
		}

		return bestMatch > 0.8 ? bestLoc : null; // 🔥 80% 이상 유사하면 성공!
	}
	
	public static BufferedImage matToBufferedImage(Mat mat) {
	    OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
	    return Java2DFrameUtils.toBufferedImage(converter.convert(mat));
	}
	
	public static BufferedImage load(String path) throws IOException, URISyntaxException {
		File devImage = new File(System.getProperty("user.dir"), path);
		if(devImage.exists()) {
			return ImageIO.read(devImage);
		}
		
		String jarPath = new File(Topwar4jApplication.class
				.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
		File productionImage = new File(jarPath, path);
		if(productionImage.exists()) {
			return ImageIO.read(productionImage);
		}
		
		throw new FileNotFoundException();
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int scale) {
	    int width = originalImage.getWidth() * scale;
	    int height = originalImage.getHeight() * scale;

	    BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
	    Graphics2D g = resizedImage.createGraphics();
	    g.drawImage(originalImage, 0, 0, width, height, null);
	    g.dispose();

	    return resizedImage;
	}

}
