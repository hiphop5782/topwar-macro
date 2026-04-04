package com.hacademy.topwar;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;

import javax.naming.ServiceUnavailableException;

import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;

import com.hacademy.topwar.util.GithubUtils;
import com.hacademy.topwar.util.RectUtils;
import com.hacademy.topwar.util.js.AllianceDefenseData;
import com.hacademy.topwar.util.js.JavascriptUtil;

public class Test36길드방어랭킹 {
	public static void main(String[] args) throws IOException, InterruptedException, AbortedByHookException, ConcurrentRefUpdateException, NoHeadException, NoMessageException, ServiceUnavailableException, UnmergedPathsException, WrongRepositoryStateException, GitAPIException {
		Rectangle rect = RectUtils.getRect(true);
		
		List<AllianceDefenseData> list = JavascriptUtil.getAdData(rect);
		JavascriptUtil.saveAllianceDefenseData(list);
		System.out.println("길드방어 랭킹 저장 완료");
		GithubUtils.commitAndPush("topwar-webutil-vite");
	}
}
