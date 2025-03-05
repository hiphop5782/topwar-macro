package com.hacademy.topwar;

import static org.bytedeco.opencv.global.opencv_core.CV_8UC3;
import static org.bytedeco.opencv.global.opencv_core.minMaxLoc;
import static org.bytedeco.opencv.global.opencv_imgproc.TM_CCOEFF_NORMED;
import static org.bytedeco.opencv.global.opencv_imgproc.matchTemplate;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;

public class Test10오픈CV {

	static File root;

	static {
		root = new File(System.getProperty("user.dir"), "images");
	}

	public static void main(String[] args) throws Exception {
		// 1. 현재 화면을 캡처 (파일 저장 없이 메모리에서 처리)
		BufferedImage screenImage = captureScreen();
		BufferedImage searchImage = loadTargetImage();
		ImageIO.write(screenImage, "png", new File(root, "demo/screen.png"));

		// 2. BufferedImage → JavaCV Mat 변환
		Mat screenMat = bufferedImageToMat(screenImage);
		Mat searchMat = bufferedImageToMat(searchImage);

		// 3. 이미지 서치 수행 (템플릿 매칭)
		Point matchPoint = findImage(screenMat, searchMat);

		// 4. 결과 출력
		if (matchPoint != null) {
			System.out.println("이미지 발견! 좌표: X=" + matchPoint.x() + ", Y=" + matchPoint.y());
			Robot r = new Robot();
			r.mouseMove(matchPoint.x(), matchPoint.y());
			r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		} else {
			System.out.println("이미지를 찾을 수 없습니다.");
		}
	}

	// ✅ 화면 캡처 메서드 (파일 저장 없이 처리)
	public static BufferedImage captureScreen() throws Exception {
		Robot robot = new Robot();
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		return robot.createScreenCapture(screenRect);
	}

	// ✅ BufferedImage로 대상 이미지 로드 (파일 저장 없이 가능)
	public static BufferedImage loadTargetImage() throws Exception {
		File target = new File(root, "button/hero.png");
		return ImageIO.read(new FileInputStream(target));
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
		if (maxVal.get() > 0.8) { // 유사도 80% 이상만 허용
			return new Point((int) maxLoc.x(), maxLoc.y());
		}
		return null;
	}

}
