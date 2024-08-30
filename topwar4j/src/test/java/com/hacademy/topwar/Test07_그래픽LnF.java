package com.hacademy.topwar;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.hacademy.topwar.ui.MainFrame;
import com.hacademy.topwar.util.GraphicUtils;

public class Test07_그래픽LnF {
	public static void main(String[] args) throws Exception {
//		[1] FlatLaf
//		FlatLightLaf.setup();
		FlatMacLightLaf.setup();
		
//		[2] Google Material UI LnF (한글안됨 이슈)
//		try {
//			UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialLiteTheme()));
//		}
//		catch(Exception e) {}
		
//		[3] Substance LnF (컴포넌트 미지원 이슈)
//		try {
//			UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
//		}
//		catch(Exception e) {}
		
		MainFrame frame = new MainFrame();
		GraphicUtils.runWindow("main", frame);
	}
}
