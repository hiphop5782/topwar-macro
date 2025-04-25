package com.hacademy.topwar;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import org.bytedeco.opencv.opencv_core.Mat;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;

import com.hacademy.topwar.ui.ScreenRectDialog;
import com.hacademy.topwar.util.ImageProcessor;
import com.hacademy.topwar.util.ImageUtils;
import com.hacademy.topwar.util.Mouse;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class Test15카르츠캡쳐 {
	private static File dir = new File("ocr/kartz");
	static {
		if(dir.exists()) {
			remove(dir);
		}
		dir.mkdirs();
	}
	public static void main(String[] args) throws Exception {
		Rectangle baseRect = ScreenRectDialog.showDialog();
		
		GlobalKeyboardHook hook = new GlobalKeyboardHook(false);
		hook.addKeyListener(new GlobalKeyAdapter() {
			@Override
			public void keyReleased(GlobalKeyEvent event) {
				switch(event.getVirtualKeyCode()) {
				case GlobalKeyEvent.VK_ESCAPE:
					System.exit(0);
				}
			}
		});
		
		LocalDate today = LocalDate.now();
		String dateStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM"));
		
		int offsetX = 82;
		int beginY = 142;
		int offsetY = 85;
		
		//이동렉 방지를 위해 미리 끝까지 다녀오기
		for(int i=0; i < 124; i++) {
			Mouse.create().move(baseRect.x + 250, baseRect.y + 350).wheelDown(46).hold(0.2f);
		}
		for(int i=0; i < 125; i++) {
			Mouse.create().move(baseRect.x + 250, baseRect.y + 350).wheelUp(46).hold(0.2f);
		}
		
		Thread.sleep(5000L);
		
		//1명씩 찍을 수가 없기 때문에 4명씩 캡쳐 후 분할(125회)
		int diff = 8;//클리어인 경우와 아닌 경우의 오차
		int innerY = 26, offset = 85;
		int rank = 1;
		for(int i=0; i < 125; i++) {
			BufferedImage bm = ImageUtils.captureScreen(
					new Rectangle(baseRect.x + offsetX, baseRect.y + beginY, 333, 332));
			saveImageWithoutProcess(bm, i, "origin");
			
			for(int k=0; k < 4; k++) {
				BufferedImage card = crop(bm, 0, bm.getHeight() / 4 * k , 333, 83);
				BufferedImage server = crop(bm, 171, innerY + offset * k, 43, 15);
				BufferedImage stage = crop(bm, 225, innerY + 18 + offset * k, 25, 15);
				if(isClearStage(server)) {
					server = crop(bm, 171, innerY + offset * k + diff, 43, 15);
					stage = crop(bm, 230, innerY + 18 + offset * k + diff, 25, 15);
				}
				else {
					BufferedImage damage = crop(bm, 176, innerY + 34 + offset * k, 68, 15);
					saveImageWithProcess(damage, rank, "damage");
				}
				saveImageWithoutProcess(card, rank, "card");
				saveImageWithProcess(server, rank, "server");
				saveImageWithProcess(stage, rank, "stage");
				rank++;
			}
			if(i < 123) {
				Mouse.create().move(baseRect.x+250, baseRect.y+350)
					.wheelDown(46).hold(0.2f);
			}
			else {
				Mouse.create().move(baseRect.x+250, baseRect.y+350)
				.wheelDown(35).hold(0.2f);
				Mouse.create().hold(1f);
				beginY = 224;
			}
		}
		
		//OCR
		
		
		System.exit(0);
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
	
	public static void saveImageWithoutProcess(BufferedImage image, int rank, String type) throws IOException {
		ImageIO.write(image, "png", new File("./ocr/kartz/kartz-"+type+"-"+rank+".png"));
	}
	public static void saveImageWithProcess(BufferedImage image, int rank, String type) {
		Mat src = ImageUtils.bufferedImageToMat(image);
		Mat gray = ImageProcessor.grayScale(src);
		Mat binary = ImageProcessor.binarizeOtsu(gray);
		Mat reverse = ImageProcessor.reverse(binary);
		imwrite("./ocr/kartz/kartz-"+rank+"-"+type+".png", reverse);
	}
    
    public static BufferedImage crop(BufferedImage src, int x, int y, int width, int height) {
        return src.getSubimage(x, y, width, height);
    }
    
    public static boolean isClearStage(BufferedImage image) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();

        int left = 0;
        int right = width;
        int y = height / 2;

        int blackPixelCount = 0;

        for (int x = left; x < right; x++) {
            Color color = new Color(image.getRGB(x, y));
            int brightness = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
//            System.out.println("brighteness = " + brightness);
            if (brightness < 50) { // 어두운 픽셀로 간주
//            	System.out.println("detected");
                blackPixelCount++;
                if (blackPixelCount > 2) return false; // 조기 종료
            }
        }

        return true;
    }
}
