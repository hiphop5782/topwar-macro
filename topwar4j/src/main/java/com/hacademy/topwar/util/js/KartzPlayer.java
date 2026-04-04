package com.hacademy.topwar.util.js;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class KartzPlayer {
	private int rank;
	private int sid;
	private String damageShow;
	private int specId;
	private String playeInfo;
}
