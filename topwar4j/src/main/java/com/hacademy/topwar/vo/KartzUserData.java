package com.hacademy.topwar.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class KartzUserData {
	private int rank;
	private int server;
	private int stage;
	private String damage;
}
