package com.hacademy.topwar.util.js;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
//{
//    "rank": 3,
//    "sid": 3453,
//    "aid": 500338874,
//    "aName": "CalculatedChaos",
//    "aTag": "53-N",
//    "totem": 15,
//    "score": 45610
//}
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class KartzAlliance {
	private int rank;
	private int sid;
	private int aid;
	@JsonProperty("aName")
	private String aName;
	@JsonProperty("aTag")
	private String aTag;
	private int totem;
	private int score;
}
