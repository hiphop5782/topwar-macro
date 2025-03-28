package com.hacademy.topwar;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
		
		int offsetX = 80;
		int beginY = 97;
		int offsetY = 85;
		
		//이동렉 방지를 위해 미리 끝까지 다녀오기
		for(int i=0; i < 124; i++) {
			Mouse.create().move(250, 350).wheelDown(46).hold();
		}
		for(int i=0; i < 125; i++) {
			Mouse.create().move(250, 350).wheelUp(46).hold();
		}
		
		Thread.sleep(5000L);
		
		//1명씩 찍을 수가 없기 때문에 4명씩 캡쳐 후 분할(125회)
		int diff = 8;//클리어인 경우와 아닌 경우의 오차
		int innerY = 25, offset = 85;
		int rank = 1;
		for(int i=0; i < 125; i++) {
			BufferedImage bm = ImageUtils.captureScreen(
					new Rectangle(baseRect.x + offsetX, baseRect.y + beginY, 330, 330));
//			ImageIO.write(bm, "png", new File(dir, "kartz-"+dateStr+"-"+(i+1)+".png"));
			
			for(int k=0; k < 4; k++) {
				BufferedImage server = crop(bm, 169, innerY + offset * k, 43, 15);
				if(isClearStage(server)) {
					server = crop(bm, 169, innerY + offset + diff, 43, 15);
				}
				ImageIO.write(server, "png", new File(dir, "kartz-server-rank-"+(rank++)+".png"));
			}
//			for(int k=0; k < 4 && i + k < size; k++) {
//				BufferedImage info = crop(bm, 124, 3, 135, 70);
////				ImageIO.write(info, "png", new File(dir, "kartz-"+dateStr+"-"+i+".png"));
//				ImageIO.write(bm, "png", new File(dir, "kartz-"+dateStr+"-"+(i+k+1)+".png"));
//			}
			if(i < 123) {
				Mouse.create().move(baseRect.x+offsetX + 250, baseRect.y+offsetY+350)
					.wheelDown(46).hold(0.2f);
			}
			else {
				Mouse.create().move(baseRect.x+offsetX + 250, baseRect.y+offsetY+350)
				.wheelDown(35).hold(0.2f);
				beginY = 177;
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
    
    public static boolean isClearStage(BufferedImage image) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();

        // 중앙 영역 (너비 30%~70%, 높이 20%~80%)
        int left = (int) (width * 0.1);
        int right = (int) (width * 0.7);
        int top = (int) (height * 0.4);
        int bottom = (int) (height * 0.6);

        int blackPixelCount = 0;

        for (int y = top; y < bottom; y++) {
            for (int x = left; x < right; x++) {
                Color color = new Color(image.getRGB(x, y));
                int brightness = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                if (brightness < 50) { // 어두운 픽셀로 간주
                    blackPixelCount++;
                    if (blackPixelCount > 5) return false; // 조기 종료
                }
            }
        }

        return true;
    }
}
