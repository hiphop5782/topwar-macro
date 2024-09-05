package com.hacademy.topwar;
import javax.swing.JOptionPane;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.hacademy.topwar.ui.MainFrame;
import com.hacademy.topwar.util.GraphicUtils;

public class Topwar4jApplication {
	public static void main(String[] args) {
		FlatMacLightLaf.setup();
		
		try {
			MainFrame frame = new MainFrame();
			GraphicUtils.runWindow("main", frame);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "실행 오류 발생\n개발자에게 문의하세요\n"+e.getMessage(), "오류 발생", JOptionPane.WARNING_MESSAGE);
		}
	}
}
