package com.hacademy.topwar.util;

public interface WinUserEx {
    // ------------------ 마우스 관련 (기존) ------------------
    int WM_LBUTTONDOWN = 0x0201;
    int WM_LBUTTONUP = 0x0202;
    int WM_MOUSEMOVE = 0x0200;
    int WM_RBUTTONDOWN = 0x0204;
    int WM_RBUTTONUP = 0x0205;

    // ------------------ 마우스 추가 상수 ------------------
    /** 마우스 휠을 위/아래로 돌릴 때 전송됩니다. */
    int WM_MOUSEWHEEL = 0x020A;
    
    /** 마우스 가운데 버튼 (휠 버튼)을 누를 때 전송됩니다. */
    int WM_MBUTTONDOWN = 0x0207; 
    
    /** 마우스 가운데 버튼 (휠 버튼)에서 손을 뗄 때 전송됩니다. */
    int WM_MBUTTONUP = 0x0208;
    
    /** 마우스 버튼을 빠르게 두 번 클릭 (더블클릭) 할 때 전송됩니다. */
    int WM_LBUTTONDBLCLK = 0x0203;
    
    // ------------------ 키보드 관련 ------------------
    /** 사용자가 시스템 키(ALT 키)를 누르지 않고 키를 누를 때 게시됩니다. */
    int WM_KEYDOWN = 0x0100;
    
    /** 사용자가 시스템 키(ALT 키)를 누르지 않고 키에서 손을 뗄 때 게시됩니다. */
    int WM_KEYUP = 0x0101; 
    
    /** 시스템 키(예: ALT)를 누르고 있을 때 키를 누르면 전송됩니다. */
    int WM_SYSKEYDOWN = 0x0104;
    
    /** 시스템 키(예: ALT)를 누르고 있을 때 키에서 손을 떼면 전송됩니다. */
    int WM_SYSKEYUP = 0x0105;
    
 // ------------------ 훅 ID 상수 (추가) ------------------
    /** 로우 레벨 마우스 훅을 설치할 때 사용되는 ID (값: 14) */
    int WH_MOUSE_LL = 14; 
    
    /** 로우 레벨 키보드 훅을 설치할 때 사용되는 ID (값: 13) */
    int WH_KEYBOARD_LL = 13;
}