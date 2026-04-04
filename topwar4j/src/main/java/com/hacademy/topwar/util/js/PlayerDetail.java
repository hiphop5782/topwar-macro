package com.hacademy.topwar.util.js;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerDetail {
	private Long nationalflag;
	private Integer gender;
	private String avatarurl;
	private String nickname;
	private String headimgurl;
	private Integer usergender;
	private String username;
}
