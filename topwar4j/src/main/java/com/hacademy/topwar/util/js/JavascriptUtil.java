package com.hacademy.topwar.util.js;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hacademy.topwar.exception.TargetNotFoundException;
import com.hacademy.topwar.util.ClipboardUtil;
import com.hacademy.topwar.util.Keyboard;
import com.hacademy.topwar.util.Mouse;

public class JavascriptUtil {
	private static ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.registerModule(new JavaTimeModule());
	} 
	
	public static List<Integer> getAllServers(Rectangle rect, boolean usePrevScreen) throws IOException, InterruptedException {
		System.out.println("[전체 서버 목록 조회중...]");
		// 주소창때문에 크롬만 가능하도록 설계
		// 월드맵 서버상태에서 확인해야함
		String script = "javascript:!function(){try{var c=cc.director.getScene().getComponentInChildren('TransferServerListPanelNew'),i=c['_allRangeIds'],f=c['_allRangeCfg'],s=[];i.forEach(id=>{s.push(...f[id].flatMap(sv=>sv.sid))});var res=JSON.stringify(s.sort((a,b)=>a-b));if(typeof copy=='function')copy(res);else{var t=document.createElement('textarea');t.value=res,document.body.appendChild(t),t.select(),document.execCommand('copy'),document.body.removeChild(t)}}catch(e){console.error(e)}}();";
		Mouse.create().clickL(136, 665).hold();
		scriptToUrlField(rect, script);
		
		Mouse.create(rect).clickL(83, 24);

		// 클립보드 읽기
		String result = ClipboardUtil.waitForClipboardData(script.substring(1)).replaceAll("[\\[\\]]", "");
		String[] tokens = result.split(",");
		List<Integer> servers = Arrays.stream(tokens).map(String::trim).map(Integer::parseInt)
				.collect(Collectors.toList());
		System.out.println("[전체 서버 목록 조회완료... 총 "+servers.size()+"개]");
		return servers;
	}
	public static List<ServerInfo> getAllServers2(Rectangle rect, boolean usePrevScreen) throws InterruptedException, JsonMappingException, JsonProcessingException {
		String script = "javascript:(function f(r,n){let fnd=(o,s)=>{if(o.name.includes(s))return o;for(let c of o.children){let r=fnd(c,s);if(r)return r}return null};let t=fnd(r,n);if(!t)return;let d=t.getComponent('WorldServerListPanel').m_data;let res=Object.keys(d).map(i=>{let s=d[i];return{serverNumber:parseInt(i),kingUid:String(s.throneUid||''),kingName:s.throneName||'',allianceTag:s.allianceTag||'',playerList:[{uid:String(s.throneUid||''),nickname:s.throneName||''}],allianceList:[{tag:s.allianceTag||''}]}}).sort((a,b)=>a.serverNumber-b.serverNumber);let j=JSON.stringify(res);if(typeof copy=='function')copy(j);else{let x=document.createElement('textarea');x.value=j;document.body.appendChild(x);x.select();document.execCommand('copy');document.body.removeChild(x);}console.log(res.length+'개 서버 매핑완료');})(cc.director.getScene(),'WorldServerListPanel');";
		
		scriptToUrlField(rect, script);
		
		// 클립보드 읽기
		String jsonStr = ClipboardUtil.waitForClipboardData(script.substring(1));
		List<ServerInfo> serverList = mapper.readValue(jsonStr, new TypeReference<List<ServerInfo>>() {});
		return serverList;
	}
	public static List<ServerInfo> getAllServersByPopularity(Rectangle rect, boolean usePrevScreen) throws InterruptedException, JsonMappingException, JsonProcessingException {
		return getAllServers2(rect, usePrevScreen).stream().sorted((a,b)->a.getPlayerList().size() - b.getPlayerList().size()).toList();
	}
	public static ServerInfo getServerInfo(Rectangle rect, int server) throws JsonMappingException, JsonProcessingException, InterruptedException {
		System.out.println("["+server+" 서버 조사 시작]");
		//개인전투력랭킹
		Mouse.create()
			.clickL(rect.x + 413, rect.y + 673).hold(1f)
			.clickL(rect.x + 151, rect.y + 329).hold(1f);
		Keyboard.create()
			.backspace(10)
			.type(String.valueOf(server));
		Mouse.create().clickL(rect.x + 247, rect.y + 401).hold(1f)
			.clickL(rect.x + 243, rect.y + 355).hold(1f)
			.clickL(rect.x + 299, rect.y + 657).hold(3f)
		;
			
		List<Player> playerList = getServerPowerRank(rect);
		List<ServerPlayerInfo> serverPlayers = new ArrayList<>();
		for(Player player : playerList) {
			PlayerDetail detail = mapper.readValue(player.getPlayerInfo(), PlayerDetail.class);
			serverPlayers.add(ServerPlayerInfo.create(player, detail));
		}
		System.out.println("→ 개인 전투력 랭킹 조회 완료");
		
		Mouse.create(rect).clickL(83, 24);//뒤로가기
		
		Mouse.create().clickL(rect.x + 400, rect.y + 657).hold(3f);//길드전투력랭킹
		
		List<ServerAllianceInfo> allianceList = getServerAllianceRank(rect);
		System.out.println("→ 동맹 전투력 랭킹 조회 완료");
		
		Mouse.create(rect).clickL(83, 24).clickL(83, 24);//뒤로가기x2
		
		return ServerInfo.builder()
					.serverNumber(server)
					.allianceList(allianceList).playerList(serverPlayers)
					.researchTime(System.currentTimeMillis())
				.build();
	}
	public static ServerInfo getServerInfo2(Rectangle rect, int serverNumber) throws JsonMappingException, JsonProcessingException, InterruptedException {
		System.out.println("["+serverNumber+"] 서버 조사 시작");
		
		moveToServer(rect, serverNumber);
		
		Mouse.create()
			.clickL(rect.x + 243, rect.y + 355).hold(1f)
			.clickL(rect.x + 299, rect.y + 657).hold(1f);
		
		List<Player> playerList = null;
		List<ServerPlayerInfo> serverPlayers = new ArrayList<>();
		for(int i=0; i < 10; i++) {
			try {
				playerList = getServerPowerRank(rect);
				for(Player player : playerList) {
					PlayerDetail detail = mapper.readValue(player.getPlayerInfo(), PlayerDetail.class);
					serverPlayers.add(ServerPlayerInfo.create(serverNumber, player, detail));
				}
				break;
			}
			catch(Exception e) { 
				System.out.println("    → 오류 발생 : 재조회 시도");
				Thread.sleep(1000);
			}
		}
		if(playerList == null) throw new TargetNotFoundException();
		System.out.println("→ 개인 전투력 랭킹 조회 완료");
		Mouse.create(rect).clickL(83, 24);
		
		Mouse.create().clickL(rect.x + 400, rect.y + 657).hold(1f);
		
		List<ServerAllianceInfo> allianceList = null;
		for(int i=0; i < 10; i++) {
			try {
				allianceList = getServerAllianceRank(rect);
				allianceList.forEach(alliance->alliance.setServer(serverNumber));
				break;
			}
			catch(Exception e) { 
				System.out.println("    → 오류 발생 : 재조회 시도");
				Thread.sleep(1000);
			}
		}
		if(allianceList == null) throw new TargetNotFoundException();
		System.out.println("→ 동맹 전투력 랭킹 조회 완료");
		Mouse.create(rect).clickL(83, 24).clickL(83, 24);//뒤로가기x2
		
		//플레이어에 동맹정보 추가
		Map<Long, ServerAllianceInfo> allianceMap = allianceList.stream().collect(Collectors.toMap(ServerAllianceInfo::getAid, alliance->alliance));
		serverPlayers.forEach(player->{
			ServerAllianceInfo alliance = allianceMap.get(player.getAllianceId());
			if(alliance == null) return;
			player.setAllianceName(alliance.getName());
			player.setAllianceTag(alliance.getTag());
		});
		
		return ServerInfo.builder()
					.allianceList(allianceList)
					.playerList(serverPlayers)
				.build();
	}
	
	public static List<Player> getServerPowerRank(Rectangle rect) throws InterruptedException, JsonMappingException, JsonProcessingException {
		String script = "javascript:!function(){try{var d=cc.director.getScene().getComponentsInChildren('WorldServerPowerRank')[0]._rankList;if(d){var s=JSON.stringify(d);if(typeof copy=='function')copy(s);else{var t=document.createElement('textarea');t.value=s;document.body.appendChild(t);t.select();document.execCommand('copy');document.body.removeChild(t)}console.log('RANK_DATA_COPIED')}else{console.error('UI_NOT_FOUND')}}catch(e){console.error(e)}}();";
		JavascriptUtil.scriptToUrlField(rect, script);
		
		String jsonStr = ClipboardUtil.waitForClipboardData(script.substring(1));
		ObjectMapper objectMapper = new ObjectMapper();
        // JSON 문자열을 Java 리스트 객체로 변환
        List<Player> rankList = objectMapper.readValue(jsonStr, new TypeReference<List<Player>>() {});
        return rankList;
	}
	
	public static List<ServerAllianceInfo> getServerAllianceRank(Rectangle rect) throws InterruptedException, JsonMappingException, JsonProcessingException {
		String script = "javascript:!function(){try{var d=cc.director.getScene().getComponentsInChildren('WorldServerAlliancePowerRank')[0]._rankList;if(d){var s=JSON.stringify(d);if(typeof copy=='function')copy(s);else{var t=document.createElement('textarea');t.value=s;document.body.appendChild(t);t.select();document.execCommand('copy');document.body.removeChild(t)}console.log('RANK_DATA_COPIED')}else{console.error('UI_NOT_FOUND')}}catch(e){console.error(e)}}();";
		JavascriptUtil.scriptToUrlField(rect, script);
		
		String result = ClipboardUtil.waitForClipboardData(script.substring(1));
		List<ServerAllianceInfo> alliances =  mapper.readValue(result, new TypeReference<List<ServerAllianceInfo>>() {});
		return alliances;
	}
	
	public static void scriptToUrlField(Rectangle rect, String script) {
		ClipboardUtil.copyToClipboard(script.substring(1));
		Mouse.create(rect).move(165, -17).clickL();
		//Keyboard.create().selectAll().backspace().type(KeyEvent.VK_J).paste().enter();
		Keyboard.create().selectAll().backspace().type("j").paste().enter();
	}

	public static void savePowerData(List<ServerInfo> datalist) throws StreamWriteException, DatabindException, IOException {
		File dir = new File("ocr/power");
		dir.mkdirs();
		
		
		List<ServerPlayerInfo> playerData = datalist.stream()
				.filter(server->server.getPlayerList() != null)
				.flatMap(server->server.getPlayerList().stream())
				.collect(Collectors.toList());
		List<ServerAllianceInfo> allianceData = datalist.stream()
				.filter(server->server.getAllianceList() != null)
				.flatMap(server->server.getAllianceList().stream())
				.collect(Collectors.toList());
		mapper.writeValue(new File(dir, "serverData.json"), datalist);
		mapper.writeValue(new File(dir, "playerData.json"), playerData);
		mapper.writeValue(new File(dir, "allianceData.json"), allianceData);
		
		File gitDir = new File(System.getProperty("user.home"), "git/topwar-webutil-vite/src/assets/json/power");
		gitDir.mkdirs();
		mapper.writeValue(new File(gitDir, "serverData.json"), datalist);
		mapper.writeValue(new File(gitDir, "playerData.json"), playerData);
		mapper.writeValue(new File(gitDir, "allianceData.json"), allianceData);
	}
	public static List<ServerInfo> loadLastTempData() throws StreamReadException, DatabindException, IOException {
		File dir = new File("ocr/power/temp");
		if(!dir.exists()) throw new FileNotFoundException();
		File[] files = dir.listFiles();
		Optional<File> recent = Arrays.stream(files).filter(file->file.isFile()).max(Comparator.comparingLong(File::lastModified));
		if(recent.isEmpty()) throw new FileNotFoundException();
		List<ServerInfo> servers = mapper.readValue(recent.get(), new TypeReference<List<ServerInfo>>() {});
		Arrays.stream(files).forEach(file->file.delete());
		return servers;
	}
	public static void saveTempData(List<ServerInfo> datalist) throws StreamWriteException, DatabindException, IOException {
		File dir = new File("ocr/power/temp");
		dir.mkdirs();
		long current = System.currentTimeMillis();
		mapper.writeValue(new File(dir, "serverData-"+current+".json"), datalist);
	}
	public static void moveToServer(Rectangle rect, int server) {
		String script = "javascript:(function f(r,n,sId){let fnd=(o,s)=>{if(o.name.includes(s))return o;for(let c of o.children){let r=fnd(c,s);if(r)return r}return null};let t=fnd(r,n);if(!t)return alert('서버 목록 창을 먼저 띄워주세요');t.getComponent('WorldServerListPanel').gotoServerById(sId);})(cc.director.getScene(),'WorldServerListPanel',"+server+");";
		scriptToUrlField(rect, script);
	}
	
	public static List<AllianceDefenseData> getAdData(Rectangle rect) throws InterruptedException, JsonMappingException, JsonProcessingException {
		String script = "javascript:(function(){try{const e=cc.director.getScene().getComponentInChildren('ActivityAllianceDefenderRankPanel');if(!e)return;const t=e.rankItems.map(((e,t)=>{const r=e.labAllianceName._string,n=r.indexOf(']');return{rank:t+1,server:parseInt(r.substring(0,n).replace(/[^0-9]+/g,'')),tag:r.substring(n+1),score:parseInt(e.labScore._string.replace(/[^0-9]+/g,'')),round:parseInt(e.labWave._string.replace(/[^0-9]+/g,''))}})).concat(e._rankList.map(((e,t)=>({rank:4+t,server:e.worldId,tag:e.tag,score:e.score,round:e.round}))));const s=JSON.stringify(t);const a=document.createElement('textarea');a.value=s;document.body.appendChild(a);a.select();document.execCommand('copy');document.body.removeChild(a);}catch(e){}})();";
		JavascriptUtil.scriptToUrlField(rect, script);
		String result = ClipboardUtil.waitForClipboardData(script.substring(1));
		return mapper.readValue(result, new TypeReference<List<AllianceDefenseData>>() {});
	}
	public static void saveAllianceDefenseData(List<AllianceDefenseData> list) throws StreamWriteException, DatabindException, IOException {
		File dir = new File("ocr/allianceDefense");
		dir.mkdirs();
		
		LocalDate today = LocalDate.now();
		mapper.writeValue(new File(dir, today+".json"), list);
		
		File gitDir  = new File(System.getProperty("user.home"), "git/topwar-webutil-vite/src/assets/json/allianceDefense");
		gitDir.mkdirs();
		mapper.writeValue(new File(gitDir, today+".json"), list);
	}
	public static void saveKartzPlayerData(KartzData data) throws StreamWriteException, DatabindException, IOException {
		File dir = new File("ocr/kartz");
		dir.mkdirs();
		
		LocalDate today = data.getTime().toLocalDate();
		String month = today.format(DateTimeFormatter.ofPattern("yyyy-MM"));
		mapper.writeValue(new File(dir, month+".json"), data);
		
		File gitDir  = new File(System.getProperty("user.home"), "git/topwar-webutil-vite/src/assets/json/kartz/history");
		gitDir.mkdirs();
		mapper.writeValue(new File(gitDir, month+".json"), data);
	}
	
	public static void saveServerList(List<ServerInfo> servers) throws StreamWriteException, DatabindException, IOException {
		File gitDir  = new File(System.getProperty("user.home"), "git/topwar-webutil-vite/src/assets/json/servers");
		gitDir.mkdirs();

		List<Integer> serverNumbersByPopularity = servers.stream()
					.sorted(
						(a,b)->{
							long ta = a.getPlayerList().stream().filter(user->user.isActive()).map(ServerPlayerInfo::getCp).reduce(0L, Long::sum);
							long tb = b.getPlayerList().stream().filter(user->user.isActive()).map(ServerPlayerInfo::getCp).reduce(0L, Long::sum);
							int gap = (int)(ta - tb);
							System.out.println("<gap = "+gap+">");
							return gap;
						}
					)
				.map(ServerInfo::getServerNumber).toList();
		List<Integer> serverNumbers = serverNumbersByPopularity.stream().sorted().toList();
		
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		mapper.writeValue(new File(gitDir, "servers-"+today+".json"), serverNumbers);
		mapper.writeValue(new File(gitDir, "servers-latest.json"), serverNumbers);
		mapper.writeValue(new File(gitDir, "servers-popular.json"), serverNumbersByPopularity);
	}
}
