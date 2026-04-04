package com.hacademy.topwar.util.js;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class ServerAllianceInfo {
	private Long score;          // 연맹 총 점수 (전투력 등)
    private String leader_name;  // 맹주 닉네임
    private String name;         // 연맹 이름
    private Integer icon;        // 연맹 아이콘 ID
    private Integer rank;        // 랭킹 순위
    private String tag;          // 연맹 태그 (예: DiY, @BC)
    private Long aid;            // 연맹 고유 ID (Alliance ID)
    private int server;
}
