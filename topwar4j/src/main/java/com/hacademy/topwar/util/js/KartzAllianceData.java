package com.hacademy.topwar.util.js;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class KartzAllianceData {
	private int rank;
	private int server;
	private String name;
	private String tag;
	private int totem;
	private int score;
	
	public static KartzAllianceData create(KartzAlliance alliance) {
		try {
			return KartzAllianceData.builder()
					.rank(alliance.getRank())
					.server(alliance.getSid())
					.name(alliance.getAName())
					.tag(alliance.getATag())
					.totem(alliance.getTotem())
					.score(alliance.getScore())
				.build();
		}
		catch(Exception e) {
			return null;
		}
	}
}
