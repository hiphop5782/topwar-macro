package com.hacademy.topwar.util;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;

import org.apache.commons.io.FileUtils;
import org.bytedeco.opencv.opencv_core.Mat;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;

public class CaptureUtils {
	private static final DecimalFormat df = new DecimalFormat("000");
	private static final GlobalKeyListener listener = new GlobalKeyAdapter() {
		@Override
		public void keyReleased(GlobalKeyEvent event) {
			switch(event.getVirtualKeyCode()) {
			case GlobalKeyEvent.VK_ESCAPE:
				System.exit(0);
			}
		}
	};
	public static File top100(Rectangle rect, int server) throws Exception {
		return top100(rect, server, true);
	}
	public static File top100(Rectangle rect, int server, boolean enableEscToQuit) throws Exception {
		//ESC 강제종료 설정
		GlobalKeyboardHook hook = null;
		if(enableEscToQuit) {
			hook = new GlobalKeyboardHook(false);
			hook.addKeyListener(listener);
		}
		
		//저장 폴더 초기화
		File dir = new File(System.getProperty("user.home"), "tw-macro/ocr/"+server);
		if(dir.exists()) {
			FileUtils.deleteDirectory(dir);
		}
		dir.mkdirs();
		
		//Top100 메뉴로 진입
		Mouse.create()
		.clickL(rect.x + 413, rect.y + 673).hold()
		.clickL(rect.x + 151, rect.y + 329);
		Keyboard.create()
		.backspace(10)
		.type(String.valueOf(server));
		Mouse.create().clickL(rect.x + 247, rect.y + 401).hold(1f)
		.clickL(rect.x + 243, rect.y + 355).hold(1f)
		.clickL(rect.x + 299, rect.y + 657).hold(3f)
		;
		
		//1~100등 캡쳐
		
		int begin = 1;
		int end = 100;
		int offsetX = 63;
		int offsetY = 58;
		
		for(int i=begin; i <= end; i++) {
			//System.out.println("y = " + (rect.y + y));
			BufferedImage bm = ImageUtils.captureScreen(new Rectangle(rect.x + offsetX, rect.y + offsetY, 374, 65));
//			ImageIO.write(bm, "png", new File(dir, "cp-"+df.format(i)+".png"));
			BufferedImage cpImage = ImageUtils.crop(bm, 181, 40, 53, 20);
//			ImageIO.write(cpImage, "png", new File(dir, "cp-"+df.format(i)+".png"));
			Mat origin = ImageUtils.bufferedImageToMat(cpImage);
			Mat result = ImageProcessor.pre(origin);
			imwrite(dir.getAbsolutePath()+"/cp-"+df.format(i)+".png", result);
			result.release();

			if(i <= 91) {
				Mouse.create().move(rect.x + 250, rect.y + 160).wheelDown(10).hold(0.02f);
			}
			else if(i < 99){
				offsetY += 74;
			}
			else {
				offsetY += 30;
				Mouse.create().move(rect.x + 250, rect.y + 160).wheelDown(6).hold(0.05f);
				Mouse.create().hold(0.5f);
			}
			
			Thread.sleep(100L);
		}
		
		//나가기
		Mouse.create()
		.clickL(rect.x + 86, rect.y + 23).hold()
		.clickL(rect.x + 86, rect.y + 23).hold();
		
		//ESC 강제종료 해제
		if(enableEscToQuit && hook != null) {
			hook.removeKeyListener(listener);
		}
		
		return dir;
	}
}



