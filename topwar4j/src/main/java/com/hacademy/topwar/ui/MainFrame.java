package com.hacademy.topwar.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	
	private JButton macroStopButton = new JButton("실행중인 매크로 중지(ESC)");
	
	private JButton areaButton = new JButton("영역설정(F2)");
	private JButton areaRemoveButton = new JButton("영역삭제(F3)");
	private JButton darkforceOnceButton = new JButton("1회");
	private JButton darkforceLoopButton = new JButton("반복");
	
	private JButton terror4kOnceButton = new JButton("1회");
	private JButton terror4kLoopButton = new JButton("10회");
	
	private JButton taskRunButton = new JButton("작업 시작");
	
	public MainFrame() throws Exception {
		this.setSize(500, 750);
		this.setAlwaysOnTop(false);
		this.setLocationByPlatform(true);
		this.setResizable(false);
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
		this.setContentPane(contentPanel);
		
		//사용할 테두리
		Border lineBorder2 = BorderFactory.createLineBorder(Color.black, 2, true);
		Border lineBorder1 = BorderFactory.createLineBorder(Color.gray, 1, true);
		
		//라벨 영역
		JPanel statusPanel = new JPanel(new GridLayout(2, 2));
		statusPanel.setBounds(5, 10, getWidth() - 25, 80);
		
		areaButton.setBackground(new Color(99, 110, 114));
		areaButton.setForeground(Color.white);
		areaButton.setFont(buttonFont);
		areaButton.addActionListener(e->{
			addScreenRect();
		});
		
		areaRemoveButton.setBackground(new Color(243, 156, 18));
		areaRemoveButton.setForeground(Color.white);
		areaRemoveButton.setFont(buttonFont);
		areaRemoveButton.addActionListener(e->{
			removeScreenRect();
		});

		screenCountLabel = new JLabel("현재 설정된 화면 : "+status.getScreenList().size(), JLabel.LEFT);
		macroExecuteCountLabel = new JLabel("매크로 실행 횟수 : 0", JLabel.LEFT);
		
		statusPanel.add(screenCountLabel);
		statusPanel.add(macroExecuteCountLabel);
		statusPanel.add(areaButton);
		statusPanel.add(areaRemoveButton);
		
		contentPanel.add(statusPanel);
		
		macroStopButton.setBounds(5, 95, getWidth() - 25, 40);
		macroStopButton.setBackground(new Color(214, 48, 49));
		macroStopButton.setForeground(Color.white);
		macroStopButton.setFont(buttonFont);
		macroStopButton.addActionListener(e->{ stopMacro(); }); 
		
		contentPanel.add(macroStopButton);
		
		//부대번호
		JPanel darkforceMarchPanel = new JPanel(new GridLayout(1, 8));
		darkforceMarchPanel.setBounds(5, macroStopButton.getY() + macroStopButton.getHeight()+5, getWidth()-25, 50);
		darkforceMarchPanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "부대번호 설정"));
		
		ButtonGroup darkforceMarchGroup = new ButtonGroup();
		for(int i=1; i <= 8; i++) {
			JRadioButton radio = new JRadioButton(i+"번", i == status.getDarkforceMarchNumber());
			radio.addActionListener(e->{
				String text = radio.getText();
				int number = Integer.parseInt(text.substring(0, 1));
				status.setDarkforceMarchNumber(number);
			});
			darkforceMarchPanel.add(radio);
			darkforceMarchGroup.add(radio);
		}
		contentPanel.add(darkforceMarchPanel);
		
		//물약사용 체크박스
		JPanel useVitPanel = new JPanel(new BorderLayout());
		useVitPanel.setBounds(5, darkforceMarchPanel.getY()+darkforceMarchPanel.getHeight()+5, getWidth() - 25, 30);
		
		JCheckBox useVit = new JCheckBox("물약 사용", status.isPotion());
		useVit.addActionListener(e->{
			status.setPotion(useVit.isSelected());
		});
		
		useVitPanel.add(useVit);
		contentPanel.add(useVitPanel);
		
		//암흑사냥
		JPanel darkforcePanel = new JPanel(null);
		darkforcePanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "암흑(DarkForce)"));
		darkforcePanel.setBounds(5, useVitPanel.getY()+useVitPanel.getHeight()+5, getWidth()-25, 140);
		
		int x = 10;
		int y = 20;
		int offset = 10;
		
		//[암흑사냥] 횟수
		JPanel darkforceCountPanel = new JPanel(new GridLayout(1, 8));
		darkforceCountPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "공격횟수"));
		darkforceCountPanel.setBounds(x, y, darkforcePanel.getWidth()-20, 50);
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
		
		y += darkforceCountPanel.getHeight() + offset;
		
		//[암흑사냥] 실행버튼
		JPanel darkforceButtonPanel = new JPanel(new GridLayout(1, 2));
		darkforceButtonPanel.setBounds(x, y, darkforcePanel.getWidth()-20, 45);
		
		darkforceOnceButton.setBackground(new Color(46, 204, 113));
		darkforceOnceButton.setForeground(Color.white);
		darkforceOnceButton.setFont(buttonFont);
		darkforceOnceButton.addActionListener(e->{
			playDarkforceMacroOnce();
		});
		darkforceButtonPanel.add(darkforceOnceButton);
		
		darkforceLoopButton.setBackground(new Color(9, 132, 227));
		darkforceLoopButton.setForeground(Color.white);
		darkforceLoopButton.setFont(buttonFont);
		darkforceLoopButton.addActionListener(e->{
			playDarkforceMacroLoop();
		});
		darkforceButtonPanel.add(darkforceLoopButton);
		
		setPlayingState(false);
		
		darkforcePanel.add(darkforceButtonPanel);
		
		//[암흑사냥] 최종추가
		contentPanel.add(darkforcePanel);

		//[테러4k]
		JPanel terror4kPanel = new JPanel(null);
		terror4kPanel.setBounds(darkforcePanel.getX(), darkforcePanel.getY() + darkforcePanel.getHeight() + 10, darkforcePanel.getWidth(), 200);
		terror4kPanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "테러(Terror-4k)"));

		x = 10; y = 20;
		
		//[테러4k] 테러레벨 선택
		JPanel terror4kLevelPanel = new JPanel(new GridLayout(1, 5));
		terror4kLevelPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "레벨(Level)"));
		terror4kLevelPanel.setBounds(x, y, terror4kPanel.getWidth() - 20, 50);
		
		ButtonGroup terror4kLevelGroup = new ButtonGroup();
		for(int i=1; i <= 5; i++) {
			JRadioButton radio = new JRadioButton(i+"레벨", i == status.getTerror4kLevel());
			radio.addActionListener(e->{
				status.setTerror4kLevel(Integer.parseInt(radio.getText().substring(0, 1)));
			});
			terror4kLevelGroup.add(radio);
			terror4kLevelPanel.add(radio);
		}
		
		terror4kPanel.add(terror4kLevelPanel);
		
		y += terror4kLevelPanel.getHeight() + 10;
		
		//[테러4k] 집결/공격 선택
		JPanel terror4kAttackTypePanel = new JPanel(new GridLayout(1, 2));
		terror4kAttackTypePanel.setBounds(x, y, terror4kPanel.getWidth() - 20, 50);
		terror4kAttackTypePanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "공격방식(Attack Type)"));
		ButtonGroup terror4kAttackTypeGroup = new ButtonGroup();
		JRadioButton terror4kAttackRally = new JRadioButton("집결", status.isTerror4kManual() == false);
		JRadioButton terror4kAttackManual = new JRadioButton("공격", status.isTerror4kManual() == true);
		terror4kAttackRally.addActionListener(e->{
			status.setTerror4kManual(false);
		});
		terror4kAttackManual.addActionListener(e->{
			status.setTerror4kManual(true);
		});
		
		terror4kAttackTypePanel.add(terror4kAttackRally);
		terror4kAttackTypePanel.add(terror4kAttackManual);
		terror4kAttackTypeGroup.add(terror4kAttackRally);
		terror4kAttackTypeGroup.add(terror4kAttackManual);
		
		terror4kPanel.add(terror4kAttackTypePanel);
		
		y += terror4kAttackTypePanel.getHeight() + 10;
		
		//테러 공격/중지 버튼
		JPanel terror4kButtonPanel = new JPanel(new GridLayout(1, 2));
		terror4kButtonPanel.setBounds(x, y, terror4kPanel.getWidth()-20, 40);
		
		terror4kOnceButton.setBackground(new Color(46, 204, 113));
		terror4kOnceButton.setForeground(Color.white);
		terror4kOnceButton.setFont(buttonFont);
		terror4kOnceButton.addActionListener(e->{
			playTerror4kMacroOnce();
		});
		
		terror4kLoopButton.setBackground(new Color(9, 132, 227));
		terror4kLoopButton.setForeground(Color.white);
		terror4kLoopButton.setFont(buttonFont);
		terror4kLoopButton.addActionListener(e->{
			playTerror4kMacroLoop();
		});
		
		terror4kButtonPanel.add(terror4kOnceButton);
		terror4kButtonPanel.add(terror4kLoopButton);
		terror4kPanel.add(terror4kButtonPanel);
		
		contentPanel.add(terror4kPanel);
		
		JPanel dailyPanel = new JPanel(null);
		dailyPanel.setBounds(terror4kPanel.getX(), terror4kPanel.getY() + terror4kPanel.getHeight() + 10, terror4kPanel.getWidth(), 120);
		dailyPanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "매일 하루에 한번씩"));
		
		JPanel dailyTaskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		dailyTaskPanel.setBounds(5, 15, dailyPanel.getWidth() - 10, 50);
		JCheckBox chk1 = new JCheckBox("VIP보상", status.isDailyVipReward());
		chk1.addActionListener(e->status.setDailyVipReward(chk1.isSelected()));
		JCheckBox chk2 = new JCheckBox("장바구니", status.isDailyBasketReward());
		chk2.addActionListener(e->status.setDailyBasketReward(chk2.isSelected()));
		JCheckBox chk3 = new JCheckBox("특별패키지", status.isDailySpecialReward());
		chk3.addActionListener(e->status.setDailySpecialReward(chk3.isSelected()));
		JCheckBox chk4 = new JCheckBox("무료다이아", status.isDailyGemReward());
		chk4.addActionListener(e->status.setDailyGemReward(chk4.isSelected()));
		JCheckBox chk5 = new JCheckBox("미지의작전", status.isDailyHeavyTrooperReward());
		chk5.addActionListener(e->status.setDailyHeavyTrooperReward(chk5.isSelected()));
		JCheckBox chk6 = new JCheckBox("사판훈련", status.isDailySandTraning());
		chk6.addActionListener(e->status.setDailySandTraning(chk6.isSelected()));
		JCheckBox chk7 = new JCheckBox("원정탐험", status.isDailyExpeditionBase());
		chk7.addActionListener(e->status.setDailyExpeditionBase(chk7.isSelected()));
		JCheckBox chk8 = new JCheckBox("섬 대작전", status.isDailyIslandBattle());
		chk8.addActionListener(e->status.setDailyIslandBattle(chk8.isSelected()));
		JCheckBox chk9 = new JCheckBox("고급모집2회", status.isDailyAdvancedIncruit());
		chk9.addActionListener(e->status.setDailyAdvancedIncruit(chk9.isSelected()));
		
		dailyTaskPanel.add(chk1);
		dailyTaskPanel.add(chk2);
		dailyTaskPanel.add(chk3);
		dailyTaskPanel.add(chk4);
		dailyTaskPanel.add(chk5);
		dailyTaskPanel.add(chk6);
		dailyTaskPanel.add(chk7);
		dailyTaskPanel.add(chk8);
		dailyTaskPanel.add(chk9);
		
		dailyPanel.add(dailyTaskPanel);
		
		taskRunButton.setBounds(5, 70, dailyPanel.getWidth()-10, 40);
		taskRunButton.setBackground(new Color(46, 204, 113));
		taskRunButton.setForeground(Color.white);
		taskRunButton.setFont(buttonFont);
		taskRunButton.addActionListener(e->{
			playTaskMacro();
		});
		dailyPanel.add(taskRunButton);
		
		contentPanel.add(dailyPanel);
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
//				case GlobalKeyEvent.VK_F5:
//					playDarkforceMacroOnce();
//					break;
				case GlobalKeyEvent.VK_ESCAPE:
					stopMacro();
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
		Rectangle screenRect = ScreenRectDialog.showDialog(MainFrame.this);
		status.getScreenList().add(screenRect);
		screenCountLabel.setText("현재 설정된 화면 : " + status.getScreenList().size());
		setPlayingState(false);
	}
	private void removeScreenRect() {
		if(status.getScreenList().isEmpty()) return;
		status.getScreenList().remove(status.getScreenList().size()-1);
		screenCountLabel.setText("현재 설정된 화면 : " + status.getScreenList().size());
		setPlayingState(false);
	}
	
	private void playDarkforceMacroOnce() {
		if(status.getScreenList().isEmpty()) return;
		if(timelines != null && timelines.playing()) return;
		
		timelines = new MacroTimelines(macroTimelinesListener);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.암흑매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		
		timelines.playOnce();
	}
	private void playDarkforceMacroLoop() {
		if(status.getScreenList().isEmpty()) return;
		if(timelines != null && timelines.playing()) return;
		
		timelines = new MacroTimelines(macroTimelinesListener);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.암흑반복매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		
		timelines.play();
	}
	private void playTerror4kMacroOnce() {
		if(status.getScreenList().isEmpty()) return;
		if(timelines != null && timelines.playing()) return;
		
		timelines = new MacroTimelines(macroTimelinesListener);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.테러매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		
		timelines.playOnce();
	}
	private void playTerror4kMacroLoop() {
		if(status.getScreenList().isEmpty()) return;
		if(timelines != null && timelines.playing()) return;
		
		timelines = new MacroTimelines(macroTimelinesListener);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.테러반복매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		
		timelines.play(10);
	}
	private void stopMacro() {
		if(timelines != null) {
			timelines.stop();
			timelines = null;
		}
	}
	private void playTaskMacro() {
		if(status.getScreenList().isEmpty()) return;
		if(timelines != null && timelines.playing()) return;
		
		timelines = new MacroTimelines(macroTimelinesListener);
		for(Rectangle screenRect : status.getScreenList()) {
//			MacroTimeline timeline = MacroTimelineFactory.사판훈련매크로(status, screenRect.getLocation());
//			MacroTimeline timeline = MacroTimelineFactory.무료보석수집매크로(status, screenRect.getLocation());
//			MacroTimeline timeline = MacroTimelineFactory.고급모집2회매크로(status, screenRect.getLocation());
//			MacroTimeline timeline = MacroTimelineFactory.미지의작전매크로(status, screenRect.getLocation());
//			MacroTimeline timeline = MacroTimelineFactory.원정탐험매크로(status, screenRect.getLocation());
//			MacroTimeline timeline = MacroTimelineFactory.섬대작전매크로(status, screenRect.getLocation());
			MacroTimeline timeline = MacroTimelineFactory.일일매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		
		timelines.playOnce();
	}
	
	private MacroTimelinesListener macroTimelinesListener = new MacroTimelinesListener() {
		@Override
		public void start(MacroTimelines timelines) {
			macroExecuteCount = 0;
			macroExecuteCountLabel.setText("매크로 실행 횟수 : " + macroExecuteCount);
			setPlayingState(true);
		}
		@Override
		public void finish(MacroTimelines timelines) {
			setPlayingState(false);
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
	private void setPlayingState(boolean isPlay) {
		if(status.getScreenList().isEmpty()) {
			areaButton.setEnabled(true);
			areaRemoveButton.setEnabled(false);
			darkforceOnceButton.setEnabled(false);
			darkforceLoopButton.setEnabled(false);
			terror4kOnceButton.setEnabled(false);
			terror4kLoopButton.setEnabled(false);
			macroStopButton.setEnabled(false);
			taskRunButton.setEnabled(false);
		}
		else {
			areaButton.setEnabled(isPlay == false);
			areaRemoveButton.setEnabled(isPlay == false);
			darkforceOnceButton.setEnabled(isPlay == false);
			darkforceLoopButton.setEnabled(isPlay == false);
			macroStopButton.setEnabled(isPlay == true);
			terror4kOnceButton.setEnabled(isPlay == false);
			terror4kLoopButton.setEnabled(isPlay == false);
			taskRunButton.setEnabled(isPlay == false);
		}
	}
	
}

