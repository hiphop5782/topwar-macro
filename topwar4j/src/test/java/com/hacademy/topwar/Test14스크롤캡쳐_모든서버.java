package com.hacademy.topwar;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;

import javax.imageio.ImageIO;

import com.hacademy.topwar.util.ImageUtils;
import com.hacademy.topwar.util.Keyboard;
import com.hacademy.topwar.util.Mouse;

public class Test14스크롤캡쳐_모든서버 {
	private static DecimalFormat df = new DecimalFormat("000");
	public static void main(String[] args) throws Exception {
		Rectangle baseRect = new Rectangle(8, 82, 500, 700);
		int[] servers = new int[] {
			148, 341, 770, 1508, 2216, 2842, 2687, 3223, 3384
		};
//		for(int server : servers) {
//			captureServerCombatPower(baseRect, server);
//		}
		for(int server : servers) {
			processOCR(server);
		}
	}
	
	public static void captureServerCombatPower(Rectangle baseRect, int server) throws Exception {
		File dir = new File("ocr/debug/"+server);
		if(dir.exists()) {
			remove(dir);
		}
		dir.mkdirs();
		
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
		for(int i=begin; i <= end; i++) {
			//System.out.println("y = " + (baseRect.y + y));
			BufferedImage bm = ImageUtils.captureScreen(new Rectangle(baseRect.x + offsetX, baseRect.y + offsetY, 400, 69));
//			ImageIO.write(bm, "png", new File(dir, "cp-"+i+".png"));
			BufferedImage cpImage = crop(bm, 195, 43, 53, 17);
			ImageIO.write(cpImage, "png", new File(dir, "cp-"+df.format(i)+".png"));
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
    
    public static void processOCR(int server) throws IOException, InterruptedException {
        // 파이썬 실행 명령
        ProcessBuilder builder = new ProcessBuilder(
            "python",
            "D:/sub/ocr-workspace/ocr.py",
            String.valueOf(server)
        );

        builder.inheritIO(); // 파이썬 로그 출력
        Process process = builder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("✅ OCR 실행 완료!");
        } else {
            System.out.println("❌ OCR 실패 (코드: " + exitCode + ")");
        }
    }
}