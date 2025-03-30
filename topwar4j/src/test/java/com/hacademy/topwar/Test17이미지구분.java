package com.hacademy.topwar;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test17이미지구분 {
	public static void main(String[] args) throws IOException {
		File baseDir = new File("C:\\Users\\hwang\\git\\topwar-macro\\topwar4j\\ocr\\kartz");
		BufferedImage img1 = ImageIO.read(new File(baseDir, "kartz-server-rank-1.png"));
		BufferedImage img2 = ImageIO.read(new File(baseDir, "kartz-server-rank-5.png"));
		
		System.out.println("img1 = " + isClearStage(img1));
		System.out.println("img2 = " + isClearStage(img2));
	}
	
	public static boolean isClearStage(BufferedImage image) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();

        // 중앙 영역 (너비 30%~70%, 높이 20%~80%)
        int left = 0;
        int right = width;
        int y = height / 2;

        int blackPixelCount = 0;

        for (int x = left; x < right; x++) {
            Color color = new Color(image.getRGB(x, y));
            int brightness = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
            System.out.println("brighteness = " + brightness+", count = " +blackPixelCount);
            if (brightness < 50) { // 어두운 픽셀로 간주
//            	System.out.println("detected");
                blackPixelCount++;
                if (blackPixelCount > 5) return false; // 조기 종료
            }
        }

        return true;
    }
}
