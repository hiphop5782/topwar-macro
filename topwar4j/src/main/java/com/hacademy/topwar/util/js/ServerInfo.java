package com.hacademy.topwar.util.js;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ServerInfo {
	private int serverNumber;
	private String kingUid;
	private String kingName;
	private String allianceTag;
	private Long researchTime;
	private List<ServerPlayerInfo> playerList;
	private List<ServerAllianceInfo> allianceList;
}
