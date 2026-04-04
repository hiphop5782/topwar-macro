package com.hacademy.topwar.util.js;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

//{
//  "moreHeadImgs": [
//    "https://knight-cdn.akamaized.net/headimg/ea9463d371b692c24b79a28915a24012_1735872865.jpg?v=1735872866295"
//  ],
//  "nationalflag": 233,
//  "gender": 0,
//  "avatarurl": null,
//  "nickname": "",
//  "headimgurl": null,
//  "cn": 0,
//  "usergender": 1,
//  "headimgurl_custom": "https://knight-cdn.akamaized.net/headimg/ea9463d371b692c24b79a28915a24012_1735872865.jpg?v=1735872866295",
//  "username": "HENDO"
//}
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class KartzPlayerDetail {
	private String headimgurl_custom;
	private String username;
	private int usergender;
	private int gender;
	private String nickname;
	private String headimgurl;
	private String avatarurl;
	private int cn;
	private int nationalFlag;
}
