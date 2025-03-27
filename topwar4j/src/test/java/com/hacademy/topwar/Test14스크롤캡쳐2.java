package com.hacademy.topwar;

import static org.bytedeco.opencv.global.opencv_core.bitwise_not;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.MORPH_RECT;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.dilate;
import static org.bytedeco.opencv.global.opencv_imgproc.getStructuringElement;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;

import com.hacademy.topwar.util.ImageUtils;
import com.hacademy.topwar.util.Mouse;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;


public class Test14스크롤캡쳐2 {
	public static void main(String[] args) throws Exception {
		File dir = new File("ocr/debug");
		remove(dir);
		
		dir.mkdirs();
		
		for(int i=0; i < 50; i++) {
			BufferedImage bm = ImageUtils.captureScreen(new Rectangle(50, 93, 400, 69));
			User user = extract(bm, true);
			//System.out.println("nickname = " + user.nickname);
			System.out.println("cp = " + user.cp);
			Mouse.create().move(200, 200).wheelDown(10);
			Thread.sleep(10);
		}
	}
	
	public static void remove(File file) {
		if(file.isFile()) {
			file.delete();
		}
		else if(file.isDirectory()) {
			for(File inner : file.listFiles()) {
				remove(inner);
			}
			file.delete();
		}
	}
	
	
	public static BufferedImage p(BufferedImage src) {
    	BufferedImage b1 = upscale(src, 4);
    	BufferedImage b2 = binarize(b1, 125);
    	return b2;
    }
    
    public static BufferedImage crop(BufferedImage src, int x, int y, int width, int height) {
        return src.getSubimage(x, y, width, height);
    }
    
    public static BufferedImage upscale(BufferedImage image, int scale) {
        int width = image.getWidth() * scale;
        int height = image.getHeight() * scale;
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }

    
    public static BufferedImage binarize(BufferedImage image, int threshold) {
        BufferedImage binary = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
                int gray = (r + g + b) / 3;
                int value = (gray < threshold) ? 0x000000 : 0xFFFFFF;
                binary.setRGB(x, y, (0xFF << 24) | value);
            }
        }
        return binary;
    }
    public static User extract(BufferedImage inputImage, boolean debug) throws TesseractException, IOException {
    	//BufferedImage nicknameImage = crop(inputImage, 145, 9, 250, 25);
    	BufferedImage cpImage = crop(inputImage, 195, 43, 53, 24);
    	User user = new User();
    	//user.nickname = extractCharacterWithOCR(nicknameImage, debug);
    	user.cp = extractNumberWithOCR(cpImage, debug);
    	return user;
    }
    public static String extractNumberWithOCR(BufferedImage inputImage) throws TesseractException, IOException {
    	return extractNumberWithOCR(inputImage, false);
    }
    public static String extractNumberWithOCR(BufferedImage inputImage, boolean debug) throws TesseractException, IOException {
        // 1. 업스케일 (6배)
        BufferedImage upscaled = upscale(inputImage, 6);

        // 2. BufferedImage → Mat
        Mat mat = bufferedImageToMat(upscaled);

        // 3. Grayscale
        Mat gray = new Mat();
        cvtColor(mat, gray, COLOR_BGR2GRAY);

        // 4. (No dilation!) → 소수점 손실 방지
        Mat processed = gray.clone();

        // 5. 반전
        bitwise_not(processed, processed);

        // 6. Mat → BufferedImage
        BufferedImage processedImage = matToBufferedImage(processed);

        // 7. OCR 설정
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng");
        tesseract.setVariable("tessedit_char_whitelist", "0123456789.M");
        tesseract.setVariable("user_defined_dpi", "300");

        // 8. OCR 수행
        String value = tesseract.doOCR(processedImage)
            .replaceAll("[^0-9.M]", "") // 숫자, 점, M 외 제거
            .trim();

        // 9. 후처리 보정
        if (!value.endsWith("M")) value += "M";

        // 패턴 기반 보정
        if (value.matches("^\\.M$")) {
            value = "0.0M";
        } else if (value.matches("^\\dM$")) {
            value = value.charAt(0) + "0.0M";
        } else if (value.matches("^\\d{2}M$")) {
            value = value.charAt(0) + "." + value.charAt(1) + "M";
        } else if (value.matches("^\\d{3,}M$")) {
            // 74959M → 74.9M
            value = value.substring(0, 2) + "." + value.substring(2, value.length() - 1) + "M";
        }

        // 10. 디버깅 이미지 저장
        if (debug) {
            File debugDir = new File("ocr/debug");
            if (!debugDir.exists()) debugDir.mkdirs();
            ImageIO.write(processedImage, "png", new File(debugDir, "cp-" + value + ".png"));
        }

        return value;
    }


    
    public static String extractCharacterWithOCR(BufferedImage inputImage) throws TesseractException, IOException {
    	return extractCharacterWithOCR(inputImage, false);
    }
    public static String extractCharacterWithOCR(BufferedImage inputImage, boolean debug) throws TesseractException, IOException {
    	//crop image
    	BufferedImage upscaled = upscale(inputImage, 4);
    	
        // 1. BufferedImage -> Mat
        Mat mat = bufferedImageToMat(upscaled);

        // 2. Grayscale
        Mat gray = new Mat();
        cvtColor(mat, gray, COLOR_BGR2GRAY);

        // 3. Dilation
        Mat dilated = new Mat();
        Mat kernel = getStructuringElement(MORPH_RECT, new Size(2, 2));
        dilate(gray, dilated, kernel);

        // 4. (Optional) Thresholding
        bitwise_not(dilated, dilated);

        // 5. Mat -> BufferedImage
        BufferedImage processedImage = matToBufferedImage(dilated);
        
        //(debug) save image
        if(debug) {
        	ImageIO.write(processedImage, "png", new File("ocr/debug", "nickname-"+UUID.randomUUID().toString()+".png"));
        }

        // 6. OCR
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng+kor+jpn+chi_sim+rus+deu+fra+spa+ita+ara");
//        tesseract.setLanguage("eng");
//        tesseract.setVariable("tessedit_char_whitelist",
//        	    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_!@#");
        tesseract.setVariable("user_defined_dpi", "300");
        
        return tesseract.doOCR(processedImage).trim();
    }

    // Mat <-> BufferedImage 변환 함수
    public static Mat bufferedImageToMat(BufferedImage bi) {
        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
        return converterToMat.convert(java2dConverter.convert(bi));
    }

    public static BufferedImage matToBufferedImage(Mat mat) {
        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
        return java2dConverter.convert(converterToMat.convert(mat));
    }
}
