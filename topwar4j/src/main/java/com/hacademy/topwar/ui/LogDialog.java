package com.hacademy.topwar.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.hacademy.topwar.util.MonitorUtils;

import net.miginfocom.swing.MigLayout;

public class LogDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JScrollPane jsp = new JScrollPane();
	private JTextArea jta = new JTextArea();
	
	public LogDialog(Window parent) {
		super(parent);
		this.setAlwaysOnTop(true);
		this.setModal(false);
		this.setTitle("로그 출력");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.display();
	}
	
	private static LogDialog dialog;
    public static void showDialog(Window parent) {
    	if(dialog == null) {
    		dialog = new LogDialog(parent);
    	}
    	Dimension d = new Dimension(400, 400);
    	Rectangle rect = MonitorUtils.getCenterOfApplicationBounds(parent, d);
    	dialog.setBounds(rect);
    	dialog.setVisible(true);
    }
    
    public static LogDialog getInstance() {
    	return dialog;
    }
    
    public void display() {
    	JPanel panel = new JPanel(new MigLayout("wrap 1", "[grow,fill]", ""));
    	
    	JLabel label = new JLabel("<html>프로그램 로그 정보</html>");
    	panel.add(label, "grow");
    	
    	jsp.setViewportView(jta);
    	panel.add(jsp, "growy, pushy");
    	
    	JButton confirm = new JButton("닫기");
    	confirm.setBackground(new Color(9, 132, 227));
    	confirm.setForeground(Color.white);
    	confirm.addActionListener(e->this.setVisible(false));
    	panel.add(confirm);
    	
    	this.setContentPane(panel);
    }
    public void println(Object obj) {
    	jta.append(obj.toString());
    	jta.append("\n");
    }
}
