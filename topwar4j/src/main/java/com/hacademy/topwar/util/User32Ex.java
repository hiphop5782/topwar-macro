package com.hacademy.topwar.util;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.INPUT;
import com.sun.jna.platform.win32.WinUser.HOOKPROC;
import com.sun.jna.platform.win32.WinUser.MSLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

import com.sun.jna.platform.win32.WinDef.BOOL;  // UnhookWindowsHookEx 반환 타입
import com.sun.jna.platform.win32.WinDef.DWORD; // SetWindowsHookEx 스레드 ID 타입
import com.sun.jna.platform.win32.WinDef.HINSTANCE; // SetWindowsHookEx 모듈 핸들 타입
import com.sun.jna.platform.win32.WinDef.HWND; // HWND 타입
import com.sun.jna.platform.win32.WinDef.UINT; // 메시지 범위에 사용
import com.sun.jna.platform.win32.WinUser.MSG;  // 메시지 구조체

public interface User32Ex extends StdCallLibrary{
	User32Ex INSTANCE = Native.load("user32", User32Ex.class, W32APIOptions.DEFAULT_OPTIONS);
    HWND WindowFromPoint(POINT pt);
    boolean GetWindowRect(HWND hWnd, RECT rect);
    LRESULT SendMessage(HWND hWnd, int msg, WPARAM wParam, LPARAM lParam);
    boolean ScreenToClient(HWND hWnd, POINT point);
    
    int INPUT_MOUSE = 0;

    int MOUSEEVENTF_MOVE = 0x0001;
    int MOUSEEVENTF_LEFTDOWN = 0x0002;
    int MOUSEEVENTF_LEFTUP = 0x0004;
    int MOUSEEVENTF_ABSOLUTE = 0x8000;

    int SendInput(int nInputs, INPUT[] pInputs, int cbSize);
    
    interface LowLevelMouseProc extends HOOKPROC {
    	LRESULT callback(int nCode, WinDef.WPARAM wParam, MSLLHOOKSTRUCT lParam);
    }
    LRESULT CallNextHookEx(HHOOK hhk, int nCode, WPARAM wParam, Pointer lParam);
    
// ------------------ 새로 정의하는 훅 함수 ------------------
    
    /**
     * 로우 레벨 훅을 포함하여 훅 프로시저를 설치합니다.
     * @param idHook 훅 타입 (예: 14 = WH_MOUSE_LL)
     * @param lpfn 훅 프로시저 콜백 함수
     * @param hmod 모듈 핸들 (로우 레벨 훅의 경우 GetModuleHandle(null))
     * @param dwThreadId 스레드 ID (로우 레벨 훅의 경우 0)
     * @return 훅 핸들 (HHOOK)
     */
    HHOOK SetWindowsHookEx(int idHook, HOOKPROC lpfn, HINSTANCE hmod, DWORD dwThreadId);

    /**
     * 훅 체인에서 훅 프로시저를 제거합니다.
     * @param hhk 제거할 훅 핸들
     * @return 성공 시 true (nonzero), 실패 시 false (zero)
     */
    BOOL UnhookWindowsHookEx(HHOOK hhk);
    
 // ------------------ 새로 정의하는 메시지 함수 ------------------

    /**
     * 현재 스레드의 메시지 큐에서 메시지를 검색합니다.
     * 이 함수가 메시지 루프를 실행하고 훅이 이벤트를 처리할 수 있게 합니다.
     * * @param lpMsg 메시지 정보를 받을 MSG 구조체 포인터
     * @param hWnd 메시지를 검색할 윈도우 핸들 (null이면 현재 스레드의 모든 창 메시지)
     * @param wMsgFilterMin 처리할 최소 메시지 값 (0이면 메시지 필터 없음)
     * @param wMsgFilterMax 처리할 최대 메시지 값 (0이면 메시지 필터 없음)
     * @return WM_QUIT 메시지가 아니면 0이 아닌 값, WM_QUIT이면 0, 오류 시 -1
     */
    int GetMessage(MSG lpMsg, HWND hWnd, UINT wMsgFilterMin, UINT wMsgFilterMax);
    
    // 참고: JNA 표준 User32 인터페이스는 WinDef.UINT 대신 int 타입을 사용하기도 합니다.
    // 여기서는 JNA의 범용성을 따라 int를 사용하는 것이 일반적이며, 표준 User32의 정의와 일치합니다.
    // int GetMessage(MSG lpMsg, HWND hWnd, int wMsgFilterMin, int wMsgFilterMax);
    
    
    boolean EnumWindows(com.sun.jna.platform.win32.WinUser.WNDENUMPROC lpEnumFunc, Pointer data);
    
    /**
     * 윈도우 핸들(HWND)로부터 타이틀 바 텍스트를 유니코드로 가져옵니다.
     * nMaxCount는 버퍼에 들어갈 수 있는 최대 문자 수(char 단위)입니다.
     */
    int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);
    
    /**
     * 비동기 방식으로 메시지를 보냅니다. (클릭 자동화에 필수)
     */
    boolean PostMessageW(HWND hWnd, int msg, WPARAM wParam, LPARAM lParam);
}
