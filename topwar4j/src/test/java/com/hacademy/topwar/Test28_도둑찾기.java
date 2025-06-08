package com.hacademy.topwar;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;

import com.hacademy.topwar.util.ImageProcessor;
import com.hacademy.topwar.util.ImageUtils;
import com.hacademy.topwar.util.Keyboard;
import com.hacademy.topwar.util.Mouse;

public class Test28_도둑찾기 {
	public static void main(String[] args) throws Exception {
//		//감지영역 설정 및 요청
//		boolean usePrevScreen = true;
//		
//		Rectangle rect;
//		if(usePrevScreen) {
//			try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(".screen")))){
//				rect = (Rectangle)in.readObject();
//			}
//			catch(Exception e) {
//				rect = ScreenRectDialog.showDialog();
//				try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(".screen")))){
//					out.writeObject(rect);
//				}
//			}
//		}
//		else {
//			rect = ScreenRectDialog.showDialog();
//			try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(".screen")))){
//				out.writeObject(rect);
//			}
//		}
		
		Keyboard.enableEscToQuit();
		
		Mouse mouse = Mouse.create();
		Keyboard keyboard = Keyboard.create();
		
		//기지전환
		mouse	.clickL(1478, 997).hold(1f)
				.clickL(1478, 997).hold(1f);
		//시작지점으로 이동
		mouse	.clickL(98, 476).hold()
				.clickL(1322, 857).hold();
		keyboard.backspace(5).type("50").hold();
		mouse 	.clickL(1439, 857).hold();
		keyboard.backspace(5).type("15").hold();
		mouse 	.clickL(1278, 903).hold(0.5f);
		mouse	.move(1296, 548)
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				.wheelDown(1).hold()
				;
		BufferedImage bm = ImageUtils.load("images/search/robber.png");
		Mat target = ImageUtils.bufferedImageToMat(bm);
		for(int count = 0; count < 10; count++) {
			//세로줄 한번
			for(int i=0; i < 20; i++) {
				//캡쳐 후 이미지 비교
				Mat source = ImageUtils.captureScreenToMat(new Rectangle(180, 190, 2280, 610));
				System.out.println("이미지 캡쳐 완료");
				Point match = ImageUtils.matchBinaryTemplate(source, target);
				if (match != null) {
				    System.out.println("탐지 성공! 위치: " + match.x() + ", " + match.y());
				} else {
				    System.out.println("탐지 실패");
				}
				
				//드래그로 이동
				if(count % 2 == 0) {
					mouse.dragL(1750, 1000, 1750, 200).hold(1f);
				}
				else {
					mouse.dragL(1750, 200, 1750, 1000).hold(1f);
				}
			}
			mouse.dragL(2500, 500, 500, 500).hold(1f);
		}
	}
}
