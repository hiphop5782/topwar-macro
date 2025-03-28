package com.hacademy.topwar;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.hacademy.topwar.util.ImageUtils;
import com.hacademy.topwar.util.Keyboard;
import com.hacademy.topwar.util.Mouse;

public class Test14스크롤캡쳐이어붙이기 {
	public static void main(String[] args) throws Exception {
		int server = 2687;
		Rectangle baseRect = new Rectangle(8, 82, 500, 700);
		
		File dir = new File("ocr/debug");
		dir.mkdirs();
		
		File serverDir = new File(dir, String.valueOf(server));
		if(serverDir.exists()) {
			remove(serverDir);
		}
		serverDir.mkdirs();
		
		Mouse.create()
//		.clickL(116, 149).hold(1f)
//		.clickL(365, 700).hold(1f)
		.clickL(413, 702).hold()
		.clickL(243, 361);
		Keyboard.create()
		.backspace(10)
		.type(String.valueOf(server));
		Mouse.create().clickL(255, 438).hold(1f)
		.clickL(250, 400).hold(1f)
		.clickL(301, 693).hold(3f)
		;
		
		int begin = 1;
		int end = 100;
		int offsetX = 43;
		int offsetY = 6;
		DecimalFormat df = new DecimalFormat("000");
		List<BufferedImage> parts = new ArrayList<>();
		for(int i=begin; i <= end; i++) {
			//System.out.println("y = " + (baseRect.y + y));
			BufferedImage bm = ImageUtils.captureScreen(new Rectangle(baseRect.x + offsetX, baseRect.y + offsetY, 400, 69));
			parts.add(bm);
//			ImageIO.write(bm, "png", new File(dir, "cp-"+i+".png"));
//			BufferedImage cpImage = crop(bm, 195, 43, 53, 17);
//			ImageIO.write(cpImage, "png", new File(dir, "cp-"+df.format(i)+".png"));
			if(i <= 91) {
				Mouse.create().move(200, 200).wheelDown(10);
				Thread.sleep(25);
			}
			else if(i < 99){
				offsetY += 74;
			}
			else {
				offsetY += 44;
				Mouse.create().move(200, 200).wheelDown(4).hold(0.5f);
			}
		}
		
		Mouse.create()
		.clickL(81, 54).hold()
		.clickL(81, 54).hold();
		
		if(parts.isEmpty()) return;
		
		//이미지 연결
		int totalHeight = parts.get(0).getHeight() * parts.size();
		int width = parts.get(0).getTileWidth();
		
		BufferedImage result = new BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = result.getGraphics();
		
		for(int i=0; i < parts.size(); i++) {
			g.drawImage(parts.get(i), 0, i * parts.get(i).getHeight(), null);
		}
		
		g.dispose();
		
		ImageIO.write(result, "png", new File(dir, server+".png"));
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