package com.hacademy.topwar;

import java.awt.Rectangle;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.naming.ServiceUnavailableException;

import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacademy.topwar.util.ClipboardUtil;
import com.hacademy.topwar.util.GithubUtils;
import com.hacademy.topwar.util.Keyboard;
import com.hacademy.topwar.util.Mouse;
import com.hacademy.topwar.util.RectUtils;
import com.hacademy.topwar.util.js.JavascriptUtil;
import com.hacademy.topwar.util.js.KartzAlliance;
import com.hacademy.topwar.util.js.KartzAllianceData;
import com.hacademy.topwar.util.js.KartzData;
import com.hacademy.topwar.util.js.KartzPlayer;
import com.hacademy.topwar.util.js.KartzPlayerData;

public class Test37카르츠랭킹 {
	public static void main(String[] args) throws IOException, InterruptedException, AbortedByHookException, ConcurrentRefUpdateException, NoHeadException, NoMessageException, ServiceUnavailableException, UnmergedPathsException, WrongRepositoryStateException, GitAPIException {
		Rectangle rect = RectUtils.getRect(true);
		
		Keyboard.enableEscToQuit();
		
		//이동렉 방지를 위해 미리 끝까지 다녀오기
		Mouse.create().move(rect.x + 140, rect.y + 124).clickL().hold(1f);
		for(int i=0; i < 130; i++) {
			int x = (int)(Math.random() * 50) + 225;
			int y = (int)(Math.random() * 50) + 325;
			Mouse.create().move(rect.x + x, rect.y + y).wheelDown(46).hold(0.2f);
		}
		
		String script = "javascript:(function(){const d=JSON.stringify(cc.director.getScene().getComponentInChildren('EndLessPVERankPop')._data[0]);const t=document.createElement('textarea');document.body.appendChild(t);t.value=d;t.select();document.execCommand('copy');document.body.removeChild(t);})();";
		JavascriptUtil.scriptToUrlField(rect, script);
		
		String result = ClipboardUtil.waitForClipboardData(script);
		
		ObjectMapper mapper = new ObjectMapper();
		List<KartzPlayer> list = mapper.readValue(result, new TypeReference<List<KartzPlayer>>() {});
		List<KartzPlayerData> playerList = list.stream().map(KartzPlayerData::create).filter(Objects::nonNull).toList();
		System.out.println("플레이어 수 : " + playerList.size());
//		for(KartzPlayerData player : playerList) {
//			System.out.println(player);
//		}
		
		Thread.sleep(5000L);
		
		//이동렉 방지를 위해 미리 끝까지 다녀오기
		Mouse.create().move(rect.x + 360, rect.y + 124).clickL().hold(1f);
		for(int i=0; i < 500; i++) {
			int x = (int)(Math.random() * 50) + 225;
			int y = (int)(Math.random() * 50) + 325;
			Mouse.create().move(rect.x + x, rect.y + y).wheelDown(46).hold(0.25f);
		}
		
		script = "javascript:(function(){const d=JSON.stringify(cc.director.getScene().getComponentInChildren('EndLessPVERankPop')._data[2]);const t=document.createElement('textarea');document.body.appendChild(t);t.value=d;t.select();document.execCommand('copy');document.body.removeChild(t);})();";
		JavascriptUtil.scriptToUrlField(rect, script);
		
		result = ClipboardUtil.waitForClipboardData(script);
		List<KartzAlliance> list2 = mapper.readValue(result, new TypeReference<List<KartzAlliance>>() {});
		List<KartzAllianceData> allianceList = list2.stream().limit(300L)
					.map(KartzAllianceData::create).filter(Objects::nonNull).toList();
		System.out.println("동맹 개수 : " + allianceList.size());
		
		JavascriptUtil.saveKartzPlayerData(KartzData.builder()
					.time(LocalDateTime.now())
					.playerRankList(playerList)
					.allianceRankList(allianceList)
				.build());
		GithubUtils.commitAndPush("topwar-webutil-vite");
	}
}
