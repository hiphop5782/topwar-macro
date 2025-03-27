package com.hacademy.topwar;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_core.bitwise_not;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Size;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Test13오픈CV테서렉트OCR {
	public static void main(String[] args) throws Exception {
        // 1. 이미지 로드
        Mat src = imread("C:/Users/User1/Desktop/test.png");

        // 2. Grayscale
        Mat gray = new Mat();
        cvtColor(src, gray, COLOR_BGR2GRAY);

        // 3. Canny Edge
        Mat edges = new Mat();
        Canny(gray, edges, 50, 150);

        // 4. 윤곽선 찾기
        MatVector contours = new MatVector();
        findContours(edges.clone(), contours, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

        // 6. 후보 영역 검사 및 OCR 수행
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = boundingRect(contours.get(i));

            if (rect.width() > 100 && rect.height() > 40 && rect.height() < 90) {
//            	System.out.println(rect.x()+", "+rect.y()+", "+rect.width()+", "+rect.height());
                Mat roi = new Mat(src, rect);
                imwrite("ocr/block_" + i + ".png", roi);

                // OCR 수행
                BufferedImage bufferedImage = matToBufferedImage(roi);
                String nickname = extractCharacterWithOCR(bufferedImage, true);
                String cp = extractNumberWithOCR(bufferedImage, true);
                System.out.println("["+nickname+"] 전투력 : " + cp);
            }
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
    

    public static String extractNumberWithOCR(BufferedImage inputImage) throws TesseractException, IOException {
    	return extractNumberWithOCR(inputImage, false);
    }
    public static String extractNumberWithOCR(BufferedImage inputImage, boolean debug) throws TesseractException, IOException {
    	//crop image
    	BufferedImage crop = crop(inputImage, 197, 45, 100, 25);
    	
    	BufferedImage upscaled = upscale(crop, 4);
    	
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
        //bitwise_not(dilated, dilated);

        // 5. Mat -> BufferedImage
        BufferedImage processedImage = matToBufferedImage(dilated);
        
        //(debug) save image
        if(debug) {
        	ImageIO.write(processedImage, "png", new File("ocr/debug-cp.png"));
        }

        // 6. OCR (숫자 전용)
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng");
        tesseract.setVariable("tessedit_char_whitelist", "0123456789.M");
        tesseract.setVariable("user_defined_dpi", "300");

        String value = tesseract.doOCR(processedImage).trim(); 
        if(value.endsWith("M") == false) {
        	value += "M";
        }
        
        String regex = "^[2-9][0-9][0-9]M$";
        if(value.matches(regex)) {
        	String newValue = value.substring(0, 2) + "." + value.substring(2);
        	return newValue;
        }
        return value;
    }
    
    public static String extractCharacterWithOCR(BufferedImage inputImage) throws TesseractException, IOException {
    	return extractCharacterWithOCR(inputImage, false);
    }
    public static String extractCharacterWithOCR(BufferedImage inputImage, boolean debug) throws TesseractException, IOException {
    	//crop image
    	BufferedImage crop = crop(inputImage, 145, 10, 250, 30);
    	
    	BufferedImage upscaled = upscale(crop, 4);
    	
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
        	ImageIO.write(processedImage, "png", new File("ocr/debug-nickname.png"));
        }

        // 6. OCR
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        //tesseract.setLanguage("eng+kor+jpn+chi_sim+rus+deu+fra+spa+ita+ara");
        tesseract.setLanguage("eng");
        tesseract.setVariable("tessedit_char_whitelist",
        	    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_!@#");
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
