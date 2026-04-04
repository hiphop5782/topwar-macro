package com.hacademy.topwar.util.js;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {
	private Long val;           // 점수/전투력
    private Long power;         // 전투력
    private String uid;         // 유저 고유 ID
    private Integer serverId;   // 서버 번호
    private Long allianceId;		//동맹 번호
    private Integer lv;         // 레벨
    private String lang;        // 언어
    private String playerInfo;  // 내부 JSON 문자열 (닉네임 등이 들어있음)
    private Long lastLoginTime; // 마지막 로그인 시간
    private Long lastOnlineTime; //마지막 작업요청시간
    private int isOnline;		//현재 온라인 여부
}
