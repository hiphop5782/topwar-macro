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

public class NoticeInputDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JScrollPane jsp = new JScrollPane();
	private JTextArea jta = new JTextArea();
	
	public NoticeInputDialog(Window parent) {
		super(parent);
		this.setAlwaysOnTop(true);
		this.setModal(true);
		this.setTitle("알림 텍스트 입력");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.display();
	}
	
	private static NoticeInputDialog dialog;
    public static String showDialog(Window parent) {
    	if(dialog == null) {
    		dialog = new NoticeInputDialog(parent);
    	}
    	Dimension d = new Dimension(400, 400);
    	Rectangle rect = MonitorUtils.getCenterOfApplicationBounds(parent, d);
    	dialog.setBounds(rect);
    	dialog.setVisible(true);
    	return dialog.getInputText();
    }
    
    public void display() {
    	JPanel panel = new JPanel(new MigLayout("wrap 1", "[grow,fill]", ""));
    	
    	JLabel label = new JLabel("<html>작성된 내용을 한 줄씩 복사하여 자동 입력합니다.<br>너무 길면 내용이 잘릴 수 있으니 주의하세요!</html>");
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
    
    public String getInputText() {
    	String content = jta.getText().trim();
    	if(content.length() == 0) return null;
    	return content;
    }
}
