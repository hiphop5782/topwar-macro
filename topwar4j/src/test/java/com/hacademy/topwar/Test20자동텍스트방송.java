package com.hacademy.topwar;

import java.awt.event.KeyEvent;

import com.hacademy.topwar.util.Keyboard;
import com.hacademy.topwar.util.Mouse;

public class Test20자동텍스트방송 {
	public static void main(String[] args) throws InterruptedException {
		String prompt = """			
<Notice - auto repeat>
오늘의 일정입니다.
Zombie Defense (Reset-4)
Droid & Lv.6 rss (Reset-3)
<Discord> https://discord.gg/dvbqFamkk2 
<KakaoTalk> https://open.kakao.com/o/gCPaKRtg (비밀번호 10203040)
모든 공지사항은 카카오톡과 디스코드를 통해서 전달되니 가입 바랍니다
가입 시 닉네임은 자신의 게임 닉네임과 동일하게 설정해주세요
새로 오신 모든 분들 환영합니다! 같이 즐거운 시간 보내요
""";
//		while(true) {
//			Thread.sleep(60 * 60 * 1000L);
			prompt.lines().forEach(line->{
				Mouse.create().clickL(256, 736).hold();
				Keyboard.create().saveToClipboard(line)
					.paste().hold()
					.type(KeyEvent.VK_ENTER).hold(1f);
			});
//		}
	}
}
