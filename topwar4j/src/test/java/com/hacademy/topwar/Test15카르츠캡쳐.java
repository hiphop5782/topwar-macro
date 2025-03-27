package com.hacademy.topwar;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import com.hacademy.topwar.util.ImageUtils;
import com.hacademy.topwar.util.Mouse;

public class Test15카르츠캡쳐 {
	public static void main(String[] args) throws Exception {
		Rectangle baseRect = new Rectangle(8, 82, 500, 700);
		
		File dir = new File("ocr/kartz");
		if(dir.exists()) {
			remove(dir);
		}
		dir.mkdirs();
		LocalDate today = LocalDate.now();
		String dateStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM"));
		
		int size = 100;
		int offsetX = 84;
		int beginY = 144;
		int offsetY = 85;
		
		//이동렉 방지를 위해 미리 끝까지 다녀오기
		for(int i=0; i < 125; i++) {
			Mouse.create().move(250, 350).wheelDown(46).hold();
		}
		for(int i=0; i < 130; i++) {
			Mouse.create().move(250, 350).wheelUp(46).hold();
		}
		
		//1명씩 찍을 수가 없기 때문에 4명씩 캡쳐
		for(int i=0; i < size; i+=4) {
			for(int k=0; k < 4 && i + k < size; k++) {
				BufferedImage bm = ImageUtils.captureScreen(
						new Rectangle(baseRect.x + offsetX, baseRect.y + beginY + offsetY*k, 332, 80));
				//ImageIO.write(bm, "png", new File(dir, "kartz-"+dateStr+"-"+i+".png"));
				BufferedImage info = crop(bm, 124, 3, 135, 70);
				ImageIO.write(info, "png", new File(dir, "kartz-"+dateStr+"-"+i+".png"));
			}
			Mouse.create().move(baseRect.x+offsetX + 250, baseRect.y+offsetY+350)
					.wheelDown(46).hold(0.2f);
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
