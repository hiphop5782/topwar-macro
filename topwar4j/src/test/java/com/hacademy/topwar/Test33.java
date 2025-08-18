package com.hacademy.topwar;

import java.io.IOException;

import javax.naming.ServiceUnavailableException;

import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;

import com.hacademy.topwar.util.GithubUtils;

public class Test33 {
	public static void main(String[] args) throws AbortedByHookException, ConcurrentRefUpdateException, NoHeadException, NoMessageException, ServiceUnavailableException, UnmergedPathsException, WrongRepositoryStateException, IOException, GitAPIException {
		GithubUtils.commitAndPush();
	}
}
