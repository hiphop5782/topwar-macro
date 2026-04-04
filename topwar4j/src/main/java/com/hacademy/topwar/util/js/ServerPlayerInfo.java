package com.hacademy.topwar.util.js;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerPlayerInfo {
	private Long score; // 점수/전투력
	private Long cp; // 전투력
	private String uid; // 유저 고유 ID
	private Integer server; // 서버 번호
	private Integer level; // 레벨
	private String lang; // 언어
	private Long lastLogin; // 마지막 로그인 시간
	private Long lastRequest;//마지막 작업 요청 시간
	private boolean isOnline;
	private Long countryFlag;// 국기
	private Integer gender;// 성별(0:남성, 1:여성)
	private String profile;
	private String nickname;
	private Long allianceId;
	private String allianceTag;
	private String allianceName;

	public static ServerPlayerInfo create(Player player, PlayerDetail detail) {
		return ServerPlayerInfo.builder()
					.score(player.getVal())
					.cp(player.getPower())
					.level(player.getLv())
					.server(player.getServerId())
					.allianceId(player.getAllianceId())
					.uid(player.getUid())
					.lang(player.getLang())
					.lastLogin(player.getLastLoginTime())
					.lastRequest(player.getLastOnlineTime())
					.isOnline(player.getIsOnline() == 1)
					.countryFlag(detail.getNationalflag())
					.gender(detail.getGender() != null ? detail.getGender() : detail.getUsergender())
					.profile(detail.getAvatarurl() != null ? detail.getAvatarurl() : detail.getHeadimgurl())
					.nickname(detail.getUsername() != null ? detail.getUsername() : detail.getNickname())
				.build();
	}
	public static ServerPlayerInfo create(int server, Player player, PlayerDetail detail) {
		return ServerPlayerInfo.builder()
					.score(player.getVal())
					.cp(player.getPower())
					.level(player.getLv())
					.server(server)
					.allianceId(player.getAllianceId())
					.uid(player.getUid())
					.lang(player.getLang())
					.lastLogin(player.getLastLoginTime())
					.lastRequest(player.getLastOnlineTime())
					.isOnline(player.getIsOnline() == 1)
					.countryFlag(detail.getNationalflag())
					.gender(detail.getGender() != null ? detail.getGender() : detail.getUsergender())
					.profile(detail.getAvatarurl() != null ? detail.getAvatarurl() : detail.getHeadimgurl())
					.nickname(detail.getUsername() != null ? detail.getUsername() : detail.getNickname())
				.build();
	}
}
