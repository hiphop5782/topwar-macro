package com.hacademy.topwar.util;

//---------------------- JNA 및 Windows API 임포트 ----------------------
import com.sun.jna.Native; 
import com.sun.jna.Pointer; // Pointer 타입 (CallNextHookEx의 lParam에 사용)
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.LPARAM; // PostMessage에 사용
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.POINT; // 좌표 (ScreenToClient 및 MSLLHOOKSTRUCT 내부)
import com.sun.jna.platform.win32.WinDef.RECT; // EnumWindows 내부에서 GetWindowRect에 사용
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser; // WM_RBUTTONDOWN, WM_LBUTTONDOWN 등 상수
import com.sun.jna.platform.win32.WinUser.MSLLHOOKSTRUCT; // 마우스 훅 구조체

//---------------------- Java 표준 및 프로젝트 내부 임포트 ----------------------
import java.awt.Rectangle;
import java.util.List;
import com.hacademy.topwar.macro.MacroStatus;

public class MouseHookProcedure implements User32Ex.LowLevelMouseProc{
	@Override
	public LRESULT callback(int nCode, WPARAM wParam, MSLLHOOKSTRUCT lParam) {
		// 1. nCode < 0 이면 다음 훅으로 전달하고 종료
        if (nCode < 0) {
            return User32Ex.INSTANCE.CallNextHookEx(null, nCode, wParam, lParam.getPointer());
        }

        // 2. 우클릭 다운 이벤트인지 확인
        if (wParam.intValue() == WinUserEx.WM_RBUTTONDOWN) {
            
//            if (!active) {
//                return User32Ex.INSTANCE.CallNextHookEx(null, nCode, wParam, lParam.getPointer());
//            }
            
            // --- 3. 기존 로직 시작: 클릭 위치 및 타겟 창 찾기 ---
            
            MacroStatus status = PropertyManager.getMacroStatus(); 
            List<RectData> screenList = status.getScreenList();
            
            int clickX = lParam.pt.x;
            int clickY = lParam.pt.y;
            LogUtils.println("("+clickX+","+clickY+") 클릭");

            RectData trigger = null;
            for(RectData rectData : screenList) {
                if(rectData.active == false) continue;
                Rectangle inside = rectData.toRectangle();
                if(inside.contains(clickX, clickY)) {
                    trigger = rectData;
                    break;
                }
            }
            if(trigger == null) {
                 // 타겟 창이 아니면 이벤트를 막지 않고 통과
                 return User32Ex.INSTANCE.CallNextHookEx(null, nCode, wParam, lParam.getPointer());
            }

            // 내부 창 상대좌표 계산
            int relativeX = clickX - trigger.x;
            int relativeY = clickY - trigger.y;
            LogUtils.println("→ 창 기준 상대위치로 변환 : ("+relativeX+", "+relativeY+")");

            // --- 4. 기존 로직: 창 열거 및 필터링 (HANDLE 업데이트) ---
            User32.INSTANCE.EnumWindows((hwnd, data)->{
                char[] buffer = new char[512];
                User32.INSTANCE.GetWindowText(hwnd, buffer, buffer.length);
                String title = Native.toString(buffer);
                if(title.startsWith("Top War | Official Web Game")) {
                    RECT rect = new RECT();
                    User32.INSTANCE.GetWindowRect(hwnd, rect);
                    
                    Rectangle outside = rect.toRectangle();
                    for(RectData rectData : screenList) {
                        if(rectData.active == false) continue;
                        Rectangle inside = rectData.toRectangle();
                        Rectangle fixedInside = new Rectangle(inside.x + 50, inside.y + 50, inside.width - 100, inside.height-100);
                        if(outside.contains(fixedInside)) {
                            rectData.outside = outside;
                            rectData.hwnd = hwnd;
                            rectData.title = title;
                        }
                    }
                }
                return true;
            }, null);


            // --- 5. 기존 로직: 복제 클릭 메시지 전송 ---
            for(RectData rectData : screenList) {
                if(rectData.active == false || rectData.hwnd == null || rectData == trigger) continue;

                Rectangle inside = rectData.toRectangle();
                POINT pt = new POINT();
                pt.x = inside.x + relativeX;
                pt.y = inside.y + relativeY;
                int beforeX = pt.x;
                int beforeY = pt.y;
                
                try {
                    // 좌표 변환: Screen 좌표를 Client 좌표로 변환
                    User32Ex.INSTANCE.ScreenToClient(rectData.hwnd, pt);
                    int lParamValue = (pt.y << 16) | (pt.x & 0xFFFF); 

                    LogUtils.println("창 클릭: HANDLE = " + rectData.hwnd+" , Before ("+beforeX+","+beforeY+") → After("+pt.x+","+pt.y+")");

                    // PostMessage로 클릭 시도 (WinUserEx는 기존 프로젝트의 상수가 있는 클래스라 가정)
                    User32.INSTANCE.PostMessage(rectData.hwnd, WinUserEx.WM_LBUTTONDOWN, new WPARAM(0), new LPARAM(lParamValue));
                    User32.INSTANCE.PostMessage(rectData.hwnd, WinUserEx.WM_LBUTTONUP, new WPARAM(0), new LPARAM(lParamValue));
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

            // --- 6. 이벤트 차단 (핵심) ---
            // 0 또는 1을 반환하여 기본 우클릭 메뉴가 뜨는 것을 막고 이벤트를 소비합니다.
            return new LRESULT(1);
        }

        // 우클릭이 아니면 다음 훅으로 전달하여 기본 동작을 허용
        return User32Ex.INSTANCE.CallNextHookEx(null, nCode, wParam, lParam.getPointer());
	}
}
