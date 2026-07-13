package com.hacademy.topwar;

import java.awt.Rectangle;

import com.hacademy.topwar.util.ClipboardUtil;
import com.hacademy.topwar.util.Keyboard;
import com.hacademy.topwar.util.RectUtils;
import com.hacademy.topwar.util.js.JavascriptUtil;
import com.hacademy.topwar.util.js.ServerScanReader;
import com.hacademy.topwar.util.js.ServerScanResult;

public class Test39지도전체조회 {
	public static void main(String[] args) throws Exception {
		Rectangle rect = RectUtils.getRect(true);
		int server = 3223;
		//String script = "javascript:(async()=>{const a=document.createElement('textarea');Object.assign(a.style,{position:'fixed',left:'-9999px',top:'0'});document.body.appendChild(a);a.value='Scanning...';a.select();console.log('[TopWar] 스캔 시작 및 클립보드 준비...');try{const r=await TOPWAR.scanMapUnified({serverId:"+server+",startX:50,startY:50,endX:800,endY:875,stepX:50,stepY:75,scale:.27,clearBeforeStart:true,wait901Timeout:2200,quietMs:300,maxRetries:1,logEvery:1,prunePacketsEvery:0});const players=(TOPWAR.players?.()??r?.players??[]).filter(p=>p.uid&&Number(p.pointType)===1);const d={exportedAt:new Date().toISOString(),summary:{serverId:3223,count:players.length,filter:'pointType=1'},players};const t=JSON.stringify(d);a.value=t;a.select();const ok=document.execCommand('copy');a.remove();if(ok){console.log('[TopWar] 복사 성공!',d.summary,d);alert(`즉시 복사 완료! (유저: ${players.length}명)`);}else{throw new Error('execCommand 거부됨');}}catch(e){a.remove();console.error('[TopWar] 실패:',e);alert('복사 실패! 크롬 보안 정책으로 막혔을 수 있습니다. 콘솔을 확인하세요.');}})();";
		String script = "javascript:startScan("+server+");";
		JavascriptUtil.scriptToUrlField(rect, script);
		String jsonStr = ClipboardUtil.waitForClipboardData(script);
		
		ServerScanResult result = ServerScanReader.read(jsonStr);
		System.out.println(result.getSummary());
		for(ServerScanResult.Player player : result.getPlayers()) {
			System.out.println("→ "+player);
		}
	}
}
