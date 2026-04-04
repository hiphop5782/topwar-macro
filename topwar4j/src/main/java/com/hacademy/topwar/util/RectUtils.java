package com.hacademy.topwar.util;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.hacademy.topwar.ui.ScreenRectDialog;

public class RectUtils {
	public static Rectangle getRect(boolean usePrevScreen) throws IOException {
		// 감지영역 설정 및 요청
		Rectangle rect;
		if (usePrevScreen) {
			try (ObjectInputStream in = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(".screen")))) {
				rect = (Rectangle) in.readObject();
			} catch (Exception e) {
				rect = ScreenRectDialog.showDialog();
				try (ObjectOutputStream out = new ObjectOutputStream(
						new BufferedOutputStream(new FileOutputStream(".screen")))) {
					out.writeObject(rect);
				}
			}
		} else {
			rect = ScreenRectDialog.showDialog();
			try (ObjectOutputStream out = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(".screen")))) {
				out.writeObject(rect);
			}
		}
		return rect;
	}
}
