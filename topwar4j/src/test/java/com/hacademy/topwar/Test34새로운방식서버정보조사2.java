package com.hacademy.topwar;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.hacademy.topwar.ui.ScreenRectDialog;
import com.hacademy.topwar.util.GithubUtils;
import com.hacademy.topwar.util.Keyboard;
import com.hacademy.topwar.util.js.JavascriptUtil;
import com.hacademy.topwar.util.js.ServerInfo;

public class Test34새로운방식서버정보조사2 {
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		Rectangle rect = setRect(true);
		Keyboard.enableEscToQuit();
		
		List<ServerInfo> servers;
		try {
			servers = JavascriptUtil.loadLastTempData();
		}
		catch(Exception e) {
			servers = JavascriptUtil.getAllServers2(rect, true);
		}
		System.out.println("서버 개수 : " + servers.size());
		
		try {
			for(ServerInfo server : servers) {
				if(server.getPlayerList().size() == 100 && server.getAllianceList().size() == 10) continue;
				ServerInfo info = JavascriptUtil.getServerInfo2(rect, server.getServerNumber()); 
				server.setPlayerList(info.getPlayerList());
				server.setAllianceList(info.getAllianceList());
			}
			System.out.println("[알림] 모든 조회 완료");
			JavascriptUtil.savePowerData(servers);
			GithubUtils.commitAndPush("topwar-webutil-vite");
		}
		catch(Exception e) {
			System.err.println("[오류] "+e.getMessage());
			JavascriptUtil.saveTempData(servers);
		}		
		finally {
			long finish = System.currentTimeMillis();
			long time = finish - start;
			System.out.println("완료 (총 "+time/1000+"s 소요)");
			System.exit(0);
		}
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
