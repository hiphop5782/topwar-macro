package com.hacademy.topwar;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.bytedeco.opencv.opencv_core.Mat;

import com.hacademy.topwar.ui.ScreenRectDialog;
import com.hacademy.topwar.util.ImageProcessor;
import com.hacademy.topwar.util.ImageUtils;
import com.hacademy.topwar.util.Keyboard;
import com.hacademy.topwar.util.Mouse;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Test14스크롤캡쳐 {
	public static void main(String[] args) throws Exception {
		Rectangle baseRect = ScreenRectDialog.showDialog(null);
		
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
		
		int server = 3396;
		
		File dir = new File("ocr/debug/"+server);
		if(dir.exists()) {
			remove(dir);
		}
		dir.mkdirs();
		
		Mouse.create()
		.clickL(baseRect.x + 413, baseRect.y + 673).hold()
		.clickL(baseRect.x + 151, baseRect.y + 329);
		Keyboard.create()
		.backspace(10)
		.type(String.valueOf(server));
		Mouse.create().clickL(baseRect.x + 247, baseRect.y + 401).hold(1f)
		.clickL(baseRect.x + 243, baseRect.y + 355).hold(1f)
		.clickL(baseRect.x + 299, baseRect.y + 657).hold(3f)
		;
		
		int begin = 1;
		int end = 100;
		int offsetX = 63;
		int offsetY = 58;
		DecimalFormat df = new DecimalFormat("000");
		for(int i=begin; i <= end; i++) {
			//System.out.println("y = " + (baseRect.y + y));
			BufferedImage bm = ImageUtils.captureScreen(new Rectangle(baseRect.x + offsetX, baseRect.y + offsetY, 374, 65));
//			ImageIO.write(bm, "png", new File(dir, "cp-"+df.format(i)+".png"));
			BufferedImage cpImage = crop(bm, 181, 40, 53, 20);
//			ImageIO.write(cpImage, "png", new File(dir, "cp-"+df.format(i)+".png"));
			Mat origin = ImageUtils.bufferedImageToMat(cpImage);
			Mat result = ImageProcessor.pre(origin);
			imwrite(dir.getAbsolutePath()+"/cp-"+df.format(i)+".png", result);
			result.release();

			if(i <= 91) {
				Mouse.create().move(baseRect.x + 250, baseRect.y + 160).wheelDown(10).hold(0.02f);
			}
			else if(i < 99){
				offsetY += 74;
			}
			else {
				offsetY += 30;
				Mouse.create().move(baseRect.x + 250, baseRect.y + 160).wheelDown(6).hold(0.05f);
				Mouse.create().hold(0.5f);
			}
			
			Thread.sleep(100L);
		}
//		Rectangle baseRect = new Rectangle(8, 82, 500, 700);

		Mouse.create()
		.clickL(baseRect.x + 86, baseRect.y + 23).hold()
		.clickL(baseRect.x + 86, baseRect.y + 23).hold();
		
		List<String> list = ocr(dir);
		
		List<Double> fixList = fix(list);
		fixList.stream().forEach(System.out::println);
	}
	
	public static double parseM(String str) {
	    str = str.replaceAll("[^0-9.]", ""); // 숫자와 .만 남김
	    if (str.isEmpty()) return 0;
	    try {
	        return Double.parseDouble(str);
	    } catch (NumberFormatException e) {
	        return 0;
	    }
	}
	
	public static List<Double> fix(List<String> datalist) {
		List<Double> cpList = new ArrayList<>();
		for(int i=1; i < datalist.size()-1; i++) {
			double prev = parseM(datalist.get(i-1));
			double value = parseM(datalist.get(i));
			double next = parseM(datalist.get(i+1));
			
			cpList.add(value);
		}
		return cpList;
	}
	
	public static String ocr(BufferedImage buf) throws TesseractException {
		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
		tesseract.setLanguage("eng");
		tesseract.setVariable("tessedit_char_whitelist", "0123456789.M");
		return tesseract.doOCR(buf).trim();
	}
	public static List<String> ocr(File dir) throws TesseractException, IOException {
		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
		tesseract.setLanguage("eng");
		tesseract.setVariable("tessedit_char_whitelist", "0123456789.M");
		
		List<String> list = new ArrayList<>();
		for(File f : dir.listFiles()) {
			BufferedImage buf = ImageIO.read(f);
			String cp = tesseract.doOCR(buf).trim();
			list.add(cp);
		}
		return list;
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