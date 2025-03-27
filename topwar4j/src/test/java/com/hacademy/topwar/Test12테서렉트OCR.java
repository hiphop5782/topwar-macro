package com.hacademy.topwar;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;

public class Test12테서렉트OCR {
	public static void main(String[] args) throws Exception{
		File target = new File("C:/Users/User1/Desktop/test.png");
		if(!target.exists()) throw new FileNotFoundException();
		
		BufferedImage image = ImageIO.read(target);
		BufferedImage upscale = upscale(image, 3);
		//BufferedImage grayscale = convertToGrayscale(image);
		//BufferedImage resized = resizeImage(grayscale, 5);
		//BufferedImage binary = applyThreshold(resized, 128);

		//create Tesseract insstance
		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
		//tesseract.setLanguage("eng+kor+jpn+chi_sim+rus+deu+fra+spa+ita+ara");
		tesseract.setLanguage("eng+kor");
		tesseract.setVariable("tessedit_char_whitelist", "0123456789.M");

		//OCR
		String result = tesseract.doOCR(upscale);
		System.out.println("<OCR Result>");
		System.out.println(result);
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
	
	public static BufferedImage convertToGrayscale(BufferedImage original) {
	    BufferedImage grayscale = new BufferedImage(
	        original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY
	    );

	    for (int y = 0; y < original.getHeight(); y++) {
	        for (int x = 0; x < original.getWidth(); x++) {
	            Color color = new Color(original.getRGB(x, y));
	            int gray = (int)(color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
	            Color newColor = new Color(gray, gray, gray);
	            grayscale.setRGB(x, y, newColor.getRGB());
	        }
	    }

	    return grayscale;
	}
	
	public static BufferedImage applyThreshold(BufferedImage grayscaleImage, int threshold) {
	    int width = grayscaleImage.getWidth();
	    int height = grayscaleImage.getHeight();
	    BufferedImage binaryImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

	    for (int y = 0; y < height; y++) {
	        for (int x = 0; x < width; x++) {
	            int rgb = grayscaleImage.getRGB(x, y);
	            int gray = rgb & 0xFF; // 흑백이므로 RGB의 R=G=B

	            if (gray < threshold) {
	                binaryImage.setRGB(x, y, 0xFF000000); // 검정
	            } else {
	                binaryImage.setRGB(x, y, 0xFFFFFFFF); // 흰색
	            }
	        }
	    }

	    return binaryImage;
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
