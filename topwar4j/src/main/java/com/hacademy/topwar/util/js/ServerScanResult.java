package com.hacademy.topwar.util.js;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerScanResult {

	private String exportedAt;
	private Summary summary;
	private List<Player> players;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Summary {
		private Integer serverId;
		private Integer count;
		private String filter;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Player {
		private String time;

		private Integer c;
		private Integer seq;

		private String uid;
		private String username;
		private String nickname;

		private Integer serverId;

		private Integer x;
		private Integer y;

		private Long pointId;
		private Integer pointType;

		private Integer level;

		private Long power;

		// 7.450999953053852e+35 같은 값 때문에 BigDecimal 권장
		private BigDecimal armyPower;

		// 데이터에서 문자열로 들어오는 경우가 있음
		private String allianceId;
		private String allianceTag;

		private String language;

		private Long nationalflag;

		private Integer gender;
		private Integer usergender;
	}
}