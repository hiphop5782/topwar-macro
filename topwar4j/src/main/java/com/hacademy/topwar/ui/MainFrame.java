package com.hacademy.topwar.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import com.hacademy.topwar.macro.MacroStatus;
import com.hacademy.topwar.macro.MacroTimeline;
import com.hacademy.topwar.macro.MacroTimelineFactory;
import com.hacademy.topwar.macro.MacroTimelines;
import com.hacademy.topwar.macro.MacroTimelinesListener;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class MainFrame extends JFrame{
	
	private MacroStatus status;
	{
		status = MacroStatus.load();
	}
	
	private JLabel screenCountLabel;
	private JLabel macroExecuteCountLabel;
	private int macroExecuteCount = 0;
	
	private MacroTimelines timelines;
	
//	private JList<String> list = new JList<>();
	
	private JButton darkforceAreaButton = new JButton("영역설정(F2)");
	private JButton darkforceAreaRemoveButton = new JButton("영역삭제(F3)");
	private JButton darkforceExecuteButton = new JButton("사냥시작 (F5)");
	private JButton darkforceStopButton = new JButton("사냥종료(F6)");
	
	public MainFrame() throws Exception {
		this.setSize(600, 300);
		this.setAlwaysOnTop(true);
		this.setLocationByPlatform(true);
		this.setTitle("TW-Macro");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitProgram();
			}
		});
		
		this.init();
	}
	
	public void init() throws Exception {
		this.menu();
		this.components();
		this.events();		
	}
	
	public void menu() {
		JMenuBar bar = new JMenuBar();
		this.setJMenuBar(bar);
		
//		파일 메뉴 - 새파일, 열기, 저장, 종료
		JMenu file = new JMenu("파일");
		bar.add(file);
		
//		JMenuItem newMacro = new JMenuItem("새 매크로");
//		newMacro.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
//		newMacro.addActionListener(e->{});
//		file.add(newMacro);
		
//		file.addSeparator();
		
		JMenuItem openMacro = new JMenuItem("매크로 열기");
		openMacro.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		openMacro.addActionListener(e->{
			JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
			int choose = chooser.showOpenDialog(this);
			if(choose == JFileChooser.APPROVE_OPTION) {
				status = MacroStatus.load(chooser.getSelectedFile());
				screenCountLabel.setText("현재 설정된 화면 : " + status.getScreenList().size());
			}
		});
		file.add(openMacro);
		
		JMenuItem saveMacro = new JMenuItem("매크로 저장");
		saveMacro.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		saveMacro.addActionListener(e->{
			JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
			int choose = chooser.showSaveDialog(this);
			if(choose == JFileChooser.APPROVE_OPTION) {
				status.save(chooser.getSelectedFile());
			}
		});
		file.add(saveMacro);
		
//		file.addSeparator();
		
		JMenuItem exit = new JMenuItem("종료");
		exit.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
		exit.addActionListener(e->{
			exitProgram();
		});
		file.add(exit);
		
//		JMenu setting = new JMenu("설정");
//		bar.add(setting);
//		
//		JMenuItem startMacro = new JMenuItem("매크로 시작");
//		startMacro.setAccelerator(KeyStroke.getKeyStroke("F5"));
//		setting.add(startMacro);
//		
//		JMenuItem stopMacro = new JMenuItem("매크로 종료");
//		stopMacro.setAccelerator(KeyStroke.getKeyStroke("F6"));
//		setting.add(stopMacro);
//		
//		JMenuItem addMouseClick = new JMenuItem("마우스클릭 추가");
//		addMouseClick.setAccelerator(KeyStroke.getKeyStroke("F7"));
//		setting.add(addMouseClick);
//		
//		JMenuItem clearMacro = new JMenuItem("매크로 삭제");
//		clearMacro.setAccelerator(KeyStroke.getKeyStroke("F8"));
//		clearMacro.addActionListener(e->{
//			timeline.clear();
//			list.setListData(new String[] {});
//		});
//		setting.add(clearMacro);
		
//		JMenu demo = new JMenu("데모");
//		bar.add(demo);
//		
//		JMenuItem darkForce = new JMenuItem("암흑사냥");
//		darkForce.addActionListener(e->{
//			DarkForceDialog dialog = new DarkForceDialog(this);
//		});
//		demo.add(darkForce);
//		
//		JMenuItem warHammer = new JMenuItem("워해머-4K");
//		demo.add(warHammer);
//		
//		JMenuItem terror = new JMenuItem("테러-4K");
//		demo.add(terror);
	}
	
	public void components() {
		Font buttonFont = new Font("", Font.BOLD, 14);
		
		//메인 패널
		JPanel contentPanel = new JPanel(null);
		
		//사용할 테두리
		Border lineBorder2 = BorderFactory.createLineBorder(Color.black, 2, true);
		Border lineBorder1 = BorderFactory.createLineBorder(Color.gray, 1, true);
		
		//[암흑사냥]
		JPanel darkforcePanel = new JPanel(null);
		darkforcePanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "암흑(DarkForce)"));
		darkforcePanel.setBounds(5, 5, getWidth()-25, 240);
		
		//[암흑사냥] 횟수
		JPanel darkforceCountPanel = new JPanel(new GridLayout(1, 8));
		darkforceCountPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "공격횟수"));
		darkforceCountPanel.setBounds(10, 30, darkforcePanel.getWidth()-20, 50);
		darkforcePanel.add(darkforceCountPanel);
		
		JRadioButton darkforceCount1 = new JRadioButton("1회", status.getDarkforceAttackCount() == 1);
		JRadioButton darkforceCount2 = new JRadioButton("5회", status.getDarkforceAttackCount() == 5);
		darkforceCount1.addActionListener(e->status.setDarkforceAttackCount(1));
		darkforceCount2.addActionListener(e->status.setDarkforceAttackCount(5));
		darkforceCountPanel.add(darkforceCount1);
		darkforceCountPanel.add(darkforceCount2);
		ButtonGroup darkforceCountGroup = new ButtonGroup();
		darkforceCountGroup.add(darkforceCount1);
		darkforceCountGroup.add(darkforceCount2);
		
		//[암흑사냥] 부대번호
		JPanel darkforceMarchPanel = new JPanel(new GridLayout(1, 8));
		darkforceMarchPanel.setBounds(10, 90, darkforcePanel.getWidth()-20, 50);
		darkforceMarchPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "부대번호"));
		
		ButtonGroup darkforceMarchGroup = new ButtonGroup();
		for(int i=1; i <= 8; i++) {
			JRadioButton radio = new JRadioButton(i+"번", i == status.getDarkforceMarchCount());
			radio.addActionListener(e->{
				String text = radio.getText();
				int number = Integer.parseInt(text.substring(0, 1));
				status.setDarkforceMarchCount(number);
			});
			darkforceMarchPanel.add(radio);
			darkforceMarchGroup.add(radio);
		}
		darkforcePanel.add(darkforceMarchPanel);
		
		//라벨 영역
		JPanel darkforceStatusPanel = new JPanel(new GridLayout(1, 2));
		darkforceStatusPanel.setBounds(10, 140, darkforcePanel.getWidth() - 20, 30);

		screenCountLabel = new JLabel("현재 설정된 화면 : "+status.getScreenList().size(), JLabel.LEFT);
		darkforceStatusPanel.add(screenCountLabel);
		
		macroExecuteCountLabel = new JLabel("매크로 실행 횟수 : 0", JLabel.LEFT);
		darkforceStatusPanel.add(macroExecuteCountLabel);
		
		darkforcePanel.add(darkforceStatusPanel);
		
		//[암흑사냥] 실행버튼
		JPanel darkforceButtonPanel = new JPanel(new GridLayout(1, 3));
		darkforceButtonPanel.setBounds(10, 180, darkforcePanel.getWidth()-20, 50);
		
		darkforceAreaButton.setBackground(new Color(99, 110, 114));
		darkforceAreaButton.setForeground(Color.white);
		darkforceAreaButton.setFont(buttonFont);
		darkforceAreaButton.addActionListener(e->{
			addScreenRect();
		});
		darkforceButtonPanel.add(darkforceAreaButton);
		
		darkforceAreaRemoveButton.setBackground(new Color(243, 156, 18));
		darkforceAreaRemoveButton.setForeground(Color.white);
		darkforceAreaRemoveButton.setFont(buttonFont);
		darkforceAreaRemoveButton.addActionListener(e->{
			removeScreenRect();
		});
		darkforceButtonPanel.add(darkforceAreaRemoveButton);
		
		darkforceExecuteButton.setBackground(new Color(46, 204, 113));
		darkforceExecuteButton.setForeground(Color.white);
		darkforceExecuteButton.setFont(buttonFont);
		darkforceExecuteButton.addActionListener(e->{
			playDarkforceMacro();
		});
		darkforceButtonPanel.add(darkforceExecuteButton);
		
		darkforceStopButton.setBackground(new Color(214, 48, 49));
		darkforceStopButton.setForeground(Color.white);
		darkforceStopButton.setFont(buttonFont);
		darkforceStopButton.addActionListener(e->{
			stopDarkforceMacro();
		});
		darkforceButtonPanel.add(darkforceStopButton);
		
		darkforceAreaButton.setEnabled(true);
		darkforceAreaRemoveButton.setEnabled(true);
		darkforceExecuteButton.setEnabled(true);
		darkforceStopButton.setEnabled(false);
		
		darkforcePanel.add(darkforceButtonPanel);
		
		//[암흑사냥] 최종추가
		contentPanel.add(darkforcePanel);
		
		this.setContentPane(contentPanel);
	}
	
	public void events() throws Exception {
		//global keyboard listener
		GlobalKeyboardHook hook = new GlobalKeyboardHook(false);
		hook.addKeyListener(new GlobalKeyAdapter() {
			@Override
			public void keyReleased(GlobalKeyEvent event) {
				switch(event.getVirtualKeyCode()) {
				case GlobalKeyEvent.VK_F2:
					addScreenRect();
					break;
				case GlobalKeyEvent.VK_F3:
					removeScreenRect();
					break;
				case GlobalKeyEvent.VK_F5:
					playDarkforceMacro();
					break;
				case GlobalKeyEvent.VK_F6:
					stopDarkforceMacro();
					break;
//				case GlobalKeyEvent.VK_F7:
//					Point location = MouseInfo.getPointerInfo().getLocation();
//					timeline.add(new MacroMouseAction(location, MacroMouseActionType.CLICK));
//					break;
//				case GlobalKeyEvent.VK_F8:
//					timeline.clear();
//					break;
				}
			}
		});
	}
	
	//상태 저장 및 프로그램 종료
	public void exitProgram() {
		status.save();
		System.exit(0);
	}
	
	private void addScreenRect() {
		Rectangle screenRect = DarkForceDialog.showDialog(MainFrame.this);
		status.getScreenList().add(screenRect);
		screenCountLabel.setText("현재 설정된 화면 : " + status.getScreenList().size());
	}
	private void removeScreenRect() {
		if(status.getScreenList().isEmpty()) return;
		status.getScreenList().remove(status.getScreenList().size()-1);
		screenCountLabel.setText("현재 설정된 화면 : " + status.getScreenList().size());
	}
	
	private void playDarkforceMacro() {
		if(status.getScreenList().isEmpty()) return;
		if(timelines != null && timelines.playing()) return;
		
		timelines = new MacroTimelines(macroTimelinesListener);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.getDarkforceMacro(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		
		timelines.play();
	}
	private void stopDarkforceMacro() {
		timelines.stop();
	}
	
	private MacroTimelinesListener macroTimelinesListener = new MacroTimelinesListener() {
		@Override
		public void start(MacroTimelines timelines) {
			macroExecuteCount = 0;
			macroExecuteCountLabel.setText("매크로 실행 횟수 : " + macroExecuteCount);
			darkforceAreaButton.setEnabled(false);
			darkforceAreaRemoveButton.setEnabled(false);
			darkforceExecuteButton.setEnabled(false);
			darkforceStopButton.setEnabled(true);
		}
		@Override
		public void finish(MacroTimelines timelines) {
			darkforceAreaButton.setEnabled(true);
			darkforceAreaRemoveButton.setEnabled(true);
			darkforceExecuteButton.setEnabled(true);
			darkforceStopButton.setEnabled(false);
		}
		@Override
		public void cycleStart(MacroTimelines timelines) {
			macroExecuteCount++;
			macroExecuteCountLabel.setText("매크로 실행 횟수 : " + macroExecuteCount);
		}
		@Override
		public void cycleFinish(MacroTimelines timelines) {
			
		}
	};
	
}
