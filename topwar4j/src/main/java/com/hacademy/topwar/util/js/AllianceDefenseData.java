package com.hacademy.topwar.util.js;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class AllianceDefenseData {
	private int server;
	private String tag;
	private int rank;
	private int score;
	private int round;
}
