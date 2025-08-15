package com.hacademy.topwar.util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.naming.ServiceUnavailableException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class GithubUtils {
	public static void commitAndPush() throws IOException, AbortedByHookException, ConcurrentRefUpdateException, NoHeadException, NoMessageException, ServiceUnavailableException, UnmergedPathsException, WrongRepositoryStateException, GitAPIException {
		File repoPath = new File(System.getProperty("user.home"), "git/topwar-json");
		Scanner sc = new Scanner(new File(".", "github"));
		String token = sc.nextLine();
		sc.close();
		
		Repository repo = new FileRepositoryBuilder().setGitDir(new File(repoPath, ".git"))
				.readEnvironment().findGitDir().build();
		
		Git git = new Git(repo);
		
		// 변경된 파일 추가 (Staging)
        git.add().addFilepattern(".").call();

        // 커밋
        git.commit()
           .setMessage("JGit auto upload")
           .call();

        // 원격 저장소에 푸시
        // Git 계정 정보 (개인 액세스 토큰 사용 권장)
        UsernamePasswordCredentialsProvider credentialsProvider = 
            new UsernamePasswordCredentialsProvider("hiphop5782", token);

        git.push()
           .setCredentialsProvider(credentialsProvider)
           .call();

        System.out.println("커밋 및 푸시가 완료되었습니다.");
        
        git.close();
	}
}
