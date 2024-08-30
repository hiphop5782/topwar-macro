package com.hacademy.topwar.exception;

public class TargetNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public TargetNotFoundException() {}
	public TargetNotFoundException(String msg) {
		super(msg);
	}
}
