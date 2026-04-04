package com.hacademy.topwar;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacademy.topwar.ui.ScreenRectDialog;
import com.hacademy.topwar.util.Keyboard;
import com.hacademy.topwar.util.js.JavascriptUtil;
import com.hacademy.topwar.util.js.ServerInfo;

public class Test34새로운방식서버정보조사 {
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		
		Rectangle rect = setRect(true);
		
		Keyboard.enableEscToQuit();
		
		List<Integer> servers = JavascriptUtil.getAllServers(rect, true);
		
		ObjectMapper mapper = new ObjectMapper();
		
		List<ServerInfo> list = new ArrayList<>();
		for(int server : servers) {
			ServerInfo info = JavascriptUtil.getServerInfo(rect, server);
			list.add(info);
		}
		
		JavascriptUtil.savePowerData(list);
		
		long finish = System.currentTimeMillis();
		long time = finish - start;
		System.out.println("완료 (총 "+time/1000+"s 소요)");
    }
	
	public static Rectangle setRect(boolean usePrevScreen) throws IOException {
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
