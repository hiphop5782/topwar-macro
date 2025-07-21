package com.hacademy.topwar.util;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface User32Ex extends StdCallLibrary{
	User32Ex INSTANCE = Native.load("user32", User32Ex.class, W32APIOptions.DEFAULT_OPTIONS);
    HWND WindowFromPoint(POINT pt);
    boolean GetWindowRect(HWND hWnd, RECT rect);
    LRESULT SendMessage(HWND hWnd, int msg, WPARAM wParam, LPARAM lParam);
}
