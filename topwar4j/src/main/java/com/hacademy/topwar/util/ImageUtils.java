package com.hacademy.topwar.util;

import static org.bytedeco.opencv.global.opencv_core.CV_8UC1;
import static org.bytedeco.opencv.global.opencv_core.CV_8UC3;
import static org.bytedeco.opencv.global.opencv_core.NORM_HAMMING;
import static org.bytedeco.opencv.global.opencv_core.extractChannel;
import static org.bytedeco.opencv.global.opencv_core.minMaxLoc;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2BGR;
import static org.bytedeco.opencv.global.opencv_imgproc.THRESH_BINARY;
import static org.bytedeco.opencv.global.opencv_imgproc.THRESH_OTSU;
import static org.bytedeco.opencv.global.opencv_imgproc.TM_CCOEFF_NORMED;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.matchTemplate;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;
import static org.bytedeco.opencv.global.opencv_imgproc.threshold;
import static org.bytedeco.opencv.global.opencv_core.bitwise_and;


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
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.DMatch;
import org.bytedeco.opencv.opencv_core.DMatchVector;
import org.bytedeco.opencv.opencv_core.KeyPointVector;
//OpenCV Core
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_features2d.BFMatcher;
import org.bytedeco.opencv.opencv_features2d.ORB;

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
	public static boolean orbMatch(Mat source, Mat target) {
        // ORB 디텍터 생성
        ORB orb = ORB.create();

        // 키포인트 및 디스크립터 저장용
        KeyPointVector keypoints1 = new KeyPointVector();
        KeyPointVector keypoints2 = new KeyPointVector();
        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();

        // 특징점 검출 및 기술자 계산
        orb.detectAndCompute(source, new Mat(), keypoints1, descriptors1);
        orb.detectAndCompute(target, new Mat(), keypoints2, descriptors2);

        if (descriptors1.empty() || descriptors2.empty()) {
            System.out.println("디스크립터가 비어 있어 매칭 불가");
            return false;
        }
        
        // 매처 생성 (특징점 거리 비교)
        BFMatcher matcher = new BFMatcher(NORM_HAMMING, false);
        DMatchVector matches = new DMatchVector();
        matcher.match(descriptors1, descriptors2, matches);
        matcher.close();

        // 매치 결과를 필터링 (좋은 매치만 추출)
        double maxDist = 0;
        for (long i = 0; i < matches.size(); i++) {
            double dist = matches.get(i).distance();
            if (dist > maxDist) maxDist = dist;
        }

        List<DMatch> goodMatches = new ArrayList<>();
        for (long i = 0; i < matches.size(); i++) {
            if (matches.get(i).distance() < 0.6 * maxDist) {
                goodMatches.add(matches.get(i));
            }
        }

        System.out.println("총 매칭 수: " + matches.size());
        System.out.println("좋은 매칭 수: " + goodMatches.size());

        // 좋은 매칭이 일정 수 이상이면 포함된 것으로 판단
        return goodMatches.size() > 10;
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
	
	public static void save(String path, Mat mat) throws URISyntaxException {
		File devImage = new File(System.getProperty("user.dir"), path);
		imwrite(devImage.getAbsolutePath(), mat);
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
	
	public static BufferedImage crop(BufferedImage src, int x, int y, int width, int height) {
        return src.getSubimage(x, y, width, height);
    }
	
	/**
     * 두 이미지를 픽셀 단위로 비교 (RGB 값 기준 허용 오차 범위 내에서)
     * @param img1 첫 번째 이미지 (BGR 또는 BGRA)
     * @param img2 두 번째 이미지 (BGR 또는 BGRA)
     * @param tolerance RGB 채널별 허용 오차 (0~255)
     * @return true: 유사한 이미지 / false: 다름
     */
    public static boolean compareWithTolerance(Mat big, Mat small, int tolerance) {
    	int bw = big.cols(), bh = big.rows();
        int sw = small.cols(), sh = small.rows();

        if (sw > bw || sh > bh) return false;

        // 알파 마스크 추출
        Mat mask = new Mat();
        if (small.channels() == 4) {
            extractChannel(small, mask, 3); // alpha 채널
        } else {
            mask = Mat.ones(sh, sw, CV_8UC1).asMat(); // 마스크 전체 유효
        }

        for (int y = 0; y <= bh - sh; y++) {
            for (int x = 0; x <= bw - sw; x++) {
                if (matchesAt(big, small, mask, x, y, tolerance)) {
                    System.out.println("매칭 위치: x=" + x + ", y=" + y);
                    return true;
                }
            }
        }

        return false;
    }
    
    private static boolean matchesAt(Mat big, Mat small, Mat mask, int offsetX, int offsetY, int tolerance) {
        for (int y = 0; y < small.rows(); y++) {
            for (int x = 0; x < small.cols(); x++) {
                int m = mask.ptr(y, x).get() & 0xFF;
                if (m == 0) continue; // 배경은 무시

                for (int c = 0; c < 3; c++) { // BGR
                    int v1 = big.ptr(offsetY + y, offsetX + x).get(c) & 0xFF;
                    int v2 = small.ptr(y, x).get(c) & 0xFF;

                    if (Math.abs(v1 - v2) > tolerance) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public static Point matchBinaryTemplate(Mat source, Mat template) {
        // 1. 알파 마스크 분리
        Mat alphaMask = new Mat();
        Mat templateBGR = new Mat();

        if (template.channels() == 4) {
            cvtColor(template, templateBGR, COLOR_BGRA2BGR);
            extractChannel(template, alphaMask, 3); // 알파 채널
        } else {
            templateBGR = template.clone();
            alphaMask = new Mat(template.rows(), template.cols(), CV_8UC1, new Scalar(255));
        }

        // 2. 그레이스케일 변환
        Mat graySource = new Mat();
        Mat grayTemplate = new Mat();
        cvtColor(source, graySource, COLOR_BGR2GRAY);
        cvtColor(templateBGR, grayTemplate, COLOR_BGR2GRAY);

        // 3. Otsu 이진화
        Mat binSource = new Mat();
        Mat binTemplate = new Mat();
        threshold(graySource, binSource, 0, 255, THRESH_BINARY + THRESH_OTSU);
        threshold(grayTemplate, binTemplate, 0, 255, THRESH_BINARY + THRESH_OTSU);

        // 4. 마스크 적용된 템플릿 생성
        Mat maskedTemplate = new Mat();
        bitwise_and(binTemplate, binTemplate, maskedTemplate, alphaMask); // 마스크로 배경 제거

        // 5. 템플릿 매칭
        Mat result = new Mat();
        matchTemplate(binSource, maskedTemplate, result, TM_CCOEFF_NORMED);
        
        imwrite("images/graySource.png", graySource);
        imwrite("images/grayTemplate.png", grayTemplate);

        // 6. 결과 추출
        Point maxLoc = new Point();
        DoublePointer maxVal = new DoublePointer(1);
        minMaxLoc(result, null, maxVal, null, maxLoc, null);

        double score = maxVal.get();
        System.out.println("유사도: " + score);

        return score >= 0.6 ? maxLoc : null; // 유사도 기준은 필요 시 조절
    }
}

