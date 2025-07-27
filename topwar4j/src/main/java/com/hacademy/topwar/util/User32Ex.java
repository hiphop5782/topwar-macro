package com.hacademy.topwar.util;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinUser.INPUT;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

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
}
