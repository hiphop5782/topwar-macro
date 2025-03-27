package com.hacademy.topwar;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.hacademy.topwar.util.ImageUtils;
import com.hacademy.topwar.util.Mouse;

public class Test14스크롤캡쳐 {
	public static void main(String[] args) throws Exception {
		Rectangle baseRect = new Rectangle(2, 33, 500, 700);
		
		File dir = new File("ocr/debug");
		remove(dir);
		dir.mkdirs();
		
		int begin = 1;
		int end = 100;
		int y = 55;
		for(int i=begin; i <= end; i++) {
			System.out.println("y = " + (baseRect.y + y));
			BufferedImage bm = ImageUtils.captureScreen(new Rectangle(baseRect.x + 48, baseRect.y + y, 400, 69));
			BufferedImage cpImage = crop(bm, 195, 43, 53, 24);
			ImageIO.write(cpImage, "png", new File(dir, "cp-"+i+".png"));
			if(i <= 91) {
				Mouse.create().move(200, 200).wheelDown(10);
				Thread.sleep(25);
			}
			else if(i < 99){
				y += 74;
			}
			else {
				y += 45;
				Mouse.create().move(200, 200).wheelDown(4).hold(0.5f);
			}
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
    
    public static BufferedImage crop(BufferedImage src, int x, int y, int width, int height) {
        return src.getSubimage(x, y, width, height);
    }
}