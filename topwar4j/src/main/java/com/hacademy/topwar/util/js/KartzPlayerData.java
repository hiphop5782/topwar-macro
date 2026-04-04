package com.hacademy.topwar.util.js;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class KartzPlayerData {
	private int rank;
	private int nation;
	private int gender;
	private String nickname;
	private String profile;
	private int round;
	private int server;
	private String damage;
	
	public static KartzPlayerData create(KartzPlayer player) {
		int round = player.getSpecId() - 100600;
		ObjectMapper mapper = new ObjectMapper();
		try {
			KartzPlayerDetail detail = mapper.readValue(player.getPlayeInfo(), KartzPlayerDetail.class);
			return KartzPlayerData.builder()
						.rank(player.getRank())
						.damage(player.getDamageShow())
						.nickname(detail.getUsername())
						.gender(
							Math.max(detail.getUsergender(), detail.getGender())
						)
						.server(player.getSid())
						.profile(detail.getHeadimgurl_custom())
						.nation(detail.getNationalFlag())
						.round(round)
					.build();
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
