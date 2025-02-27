package com.hacademy.topwar.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.hacademy.topwar.macro.MacroStatus;
import com.hacademy.topwar.macro.MacroTimeline;
import com.hacademy.topwar.macro.MacroTimelineFactory;
import com.hacademy.topwar.macro.MacroTimelines;
import com.hacademy.topwar.macro.MacroTimelinesGroup;
import com.hacademy.topwar.macro.MacroTimelinesListener;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame{
	
	private MacroStatus status;
	{
		status = MacroStatus.load();
	}

	private JLabel screenCountLabel;
	private JLabel macroExecuteCountLabel;
	private int macroExecuteCount = 0;
	
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
	private MacroTimelinesGroup timelinesGroup = new MacroTimelinesGroup(macroTimelinesListener);
	
	
	private JButton macroStopButton = new JButton("실행중인 매크로 중지(ESC)");
	
	private JButton areaButton = new JButton("영역설정(F2)");
	private JButton areaRemoveButton = new JButton("영역삭제(F3)");
	private JButton darkforceOnceButton = new JButton("1회");
	private JButton darkforceTenButton = new JButton("10회");
	private JButton darkforceInputButton = new JButton("입력");
	private JButton darkforceLoopButton = new JButton("무한");
	
	private JButton warhammer1Button = new JButton("1회");
	private JButton warhammer10Button = new JButton("10회");
	private JButton warhammer15Button = new JButton("15회");
	private JButton warhammerCustomButton = new JButton("입력");
	private JButton warhammerLoopButton = new JButton("무한");
	
	private JButton terror4kOnceButton = new JButton("1회");
	private JButton terror4kFiveButton = new JButton("5회");
	private JButton terror4kLoopButton = new JButton("10회");
	
	private JButton taskRunButton = new JButton("작업 시작");
	private JButton taskCheckButton = new JButton("전체선택");
	private JButton taskReleaseButton = new JButton("전체해제");
	
	private JCheckBox dummyBox;
	
	public MainFrame() throws Exception {
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
		this.pack();
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
		JPanel contentPanel = new JPanel(new MigLayout("wrap 1, insets 10", "[grow,fill]", "10[]10[]"));
		this.setContentPane(contentPanel);
		
		//사용할 테두리
		Border lineBorder2 = BorderFactory.createLineBorder(Color.black, 2, true);
		Border lineBorder1 = BorderFactory.createLineBorder(Color.gray, 1, true);
		
		//라벨 영역
		JPanel statusPanel = new JPanel(new GridLayout(2, 2));
		
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
		
		macroStopButton.setBackground(new Color(214, 48, 49));
		macroStopButton.setForeground(Color.white);
		macroStopButton.setFont(buttonFont);
		macroStopButton.addActionListener(e->{ stopMacro(); }); 
		
		contentPanel.add(macroStopButton);
		
		//부대번호
		JPanel darkforceMarchPanel = new JPanel(new GridLayout(1, 8));
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
		
		JCheckBox useVit = new JCheckBox("물약 사용", status.isPotion());
		useVit.addActionListener(e->{
			status.setPotion(useVit.isSelected());
		});
		
		useVitPanel.add(useVit);
		contentPanel.add(useVitPanel);
		
		//암흑사냥
		JPanel darkforcePanel = new JPanel(new MigLayout("inset 5", "[]10[grow,fill]", ""));
		darkforcePanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "암흑(DarkForce)"));
		
		//[암흑사냥] 횟수
		JPanel darkforceCountPanel = new JPanel(new MigLayout("", "[]10[]", ""));
		darkforceCountPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "횟수"));
		
		JRadioButton darkforceCount1 = new JRadioButton("1회", status.getDarkforceAttackCount() == 1);
		JRadioButton darkforceCount2 = new JRadioButton("5회", status.getDarkforceAttackCount() == 5);
		darkforceCount1.addActionListener(e->status.setDarkforceAttackCount(1));
		darkforceCount2.addActionListener(e->status.setDarkforceAttackCount(5));
		darkforceCountPanel.add(darkforceCount1);
		darkforceCountPanel.add(darkforceCount2);
		ButtonGroup darkforceCountGroup = new ButtonGroup();
		darkforceCountGroup.add(darkforceCount1);
		darkforceCountGroup.add(darkforceCount2);
		
		//[암흑사냥] 실행버튼
		JPanel darkforceButtonPanel = new JPanel(new MigLayout("align right", "[]10[]", ""));
		
		darkforceOnceButton.setBackground(new Color(46, 204, 113));
		darkforceOnceButton.setForeground(Color.white);
		darkforceOnceButton.setFont(buttonFont);
		darkforceOnceButton.addActionListener(e->{
			playDarkforceMacroOnce();
		});
		darkforceButtonPanel.add(darkforceOnceButton);
		
		darkforceTenButton.setBackground(new Color(46, 204, 113));
		darkforceTenButton.setForeground(Color.white);
		darkforceTenButton.setFont(buttonFont);
		darkforceTenButton.addActionListener(e->{
			playDarkforceMacroLoop(10);
		});
		darkforceButtonPanel.add(darkforceTenButton);
		
		darkforceInputButton.setBackground(new Color(42, 52, 54));
		darkforceInputButton.setForeground(Color.white);
		darkforceInputButton.setFont(buttonFont);
		darkforceInputButton.addActionListener(e->{
			try {
				String input = JOptionPane.showInputDialog(MainFrame.this, "횟수 입력");
				int count = Integer.parseInt(input);
				if(count > 0) {
					playDarkforceMacroLoop(count);
				}
			}
			catch(Exception ex) {}
		});
		darkforceButtonPanel.add(darkforceInputButton);
		
		darkforceLoopButton.setBackground(new Color(9, 132, 227));
		darkforceLoopButton.setForeground(Color.white);
		darkforceLoopButton.setFont(buttonFont);
		darkforceLoopButton.addActionListener(e->{
			playDarkforceMacroLoop();
		});
		darkforceButtonPanel.add(darkforceLoopButton);
		
		darkforcePanel.add(darkforceCountPanel);
		darkforcePanel.add(darkforceButtonPanel);
		
		//[암흑사냥] 최종추가
		contentPanel.add(darkforcePanel);
		
		//[워해머4k]
		JPanel warhammer4kPanel = new JPanel(new MigLayout("inset 10, align right", "[]10[]", ""));
		warhammer4kPanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "워해머(Warhammer-4k)"));
		
		warhammer1Button.setBackground(new Color(46, 204, 113));
		warhammer1Button.setForeground(Color.white);
		warhammer1Button.setFont(buttonFont);
		warhammer1Button.addActionListener(e->{
			playWarhammerMacroOnce();
		});
		warhammer4kPanel.add(warhammer1Button);
		
		warhammer10Button.setBackground(new Color(46, 204, 113));
		warhammer10Button.setForeground(Color.white);
		warhammer10Button.setFont(buttonFont);
		warhammer10Button.addActionListener(e->{
			playWarhammerMacroLoop(10);
		});
		warhammer4kPanel.add(warhammer10Button);
		
		warhammer15Button.setBackground(new Color(46, 204, 113));
		warhammer15Button.setForeground(Color.white);
		warhammer15Button.setFont(buttonFont);
		warhammer15Button.addActionListener(e->{
			playWarhammerMacroLoop(15);
		});
		warhammer4kPanel.add(warhammer15Button);
		
		warhammerCustomButton.setBackground(new Color(42, 52, 54));
		warhammerCustomButton.setForeground(Color.white);
		warhammerCustomButton.setFont(buttonFont);
		warhammerCustomButton.addActionListener(e->{
			try {
				String input = JOptionPane.showInputDialog(MainFrame.this, "횟수 입력");
				int count = Integer.parseInt(input);
				if(count > 0) {
					playWarhammerMacroLoop(count);
				}
			}
			catch(Exception ex) {}
		});
		warhammer4kPanel.add(warhammerCustomButton);
		
		warhammerLoopButton.setBackground(new Color(9, 132, 227));
		warhammerLoopButton.setForeground(Color.white);
		warhammerLoopButton.setFont(buttonFont);
		warhammerLoopButton.addActionListener(e->{
			playWarhammerMacroLoop();
		});
		warhammer4kPanel.add(warhammerLoopButton);
		
		//[워해머4k] 최종추가
		contentPanel.add(warhammer4kPanel);

		//[테러4k]
		JPanel terror4kPanel = new JPanel(new MigLayout("inset 5", "[]10[]10[grow,fill]", ""));
		terror4kPanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "테러(Terror-4k)"));

		//[테러4k] 테러레벨 선택
		JPanel terror4kLevelPanel = new JPanel(new MigLayout("inset 2", "[]5[]", ""));
		terror4kLevelPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "레벨"));
		
		ButtonGroup terror4kLevelGroup = new ButtonGroup();
		for(int i=1; i <= 5; i++) {
			JRadioButton radio = new JRadioButton(String.valueOf(i), i == status.getTerror4kLevel());
			radio.addActionListener(e->{
				status.setTerror4kLevel(Integer.parseInt(radio.getText().substring(0, 1)));
			});
			terror4kLevelGroup.add(radio);
			terror4kLevelPanel.add(radio);
		}
		
		terror4kPanel.add(terror4kLevelPanel);
		
		//[테러4k] 집결/공격 선택
		JPanel terror4kAttackTypePanel = new JPanel(new MigLayout("inset 2", "[]5[]", ""));
//		terror4kAttackTypePanel.setBounds(x + terror4kPanel.getWidth()/2, y, terror4kPanel.getWidth() / 2 - 20, 50);
		terror4kAttackTypePanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "방식"));
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
		
//		y += terror4kAttackTypePanel.getHeight() + 10;
		
		//테러 공격/중지 버튼
		JPanel terror4kButtonPanel = new JPanel(new MigLayout("inset 5, align right", "[]10[]", ""));
//		terror4kButtonPanel.setBounds(x + terror4kPanel.getWidth()/2, y, terror4kPanel.getWidth() / 2 - 20, 45);
		
		terror4kOnceButton.setBackground(new Color(46, 204, 113));
		terror4kOnceButton.setForeground(Color.white);
		terror4kOnceButton.setFont(buttonFont);
		terror4kOnceButton.addActionListener(e->{
			playTerror4kMacroOnce();
		});
		
		terror4kFiveButton.setBackground(new Color(46, 204, 113));
		terror4kFiveButton.setForeground(Color.white);
		terror4kFiveButton.setFont(buttonFont);
		terror4kFiveButton.addActionListener(e->{
			playTerror4kMacroLoop(5);
		});
		
		terror4kLoopButton.setBackground(new Color(9, 132, 227));
		terror4kLoopButton.setForeground(Color.white);
		terror4kLoopButton.setFont(buttonFont);
		terror4kLoopButton.addActionListener(e->{
			playTerror4kMacroLoop(10);
		});
		
		terror4kButtonPanel.add(terror4kOnceButton);
		terror4kButtonPanel.add(terror4kFiveButton);
		terror4kButtonPanel.add(terror4kLoopButton);
		terror4kPanel.add(terror4kButtonPanel);
		
		contentPanel.add(terror4kPanel);
		
		JPanel dailyPanel = new JPanel(new MigLayout("wrap 1", "[grow]", ""));
		dailyPanel.setBounds(terror4kPanel.getX(), terror4kPanel.getY() + terror4kPanel.getHeight() + 10, terror4kPanel.getWidth(), 150);
		dailyPanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "일일 작업"));
		
		JPanel dailyTaskPanel = new JPanel(new MigLayout("wrap 5", "[][][][][]", ""));
		dailyTaskPanel.setBounds(5, 15, dailyPanel.getWidth() - 10, 80);
		
		List<JCheckBox> dailyTaskCheckboxes = new ArrayList<>();
		dailyTaskCheckboxes.add(new JCheckBox("VIP보상", status.isDailyVipReward()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).addActionListener(e->status.setDailyVipReward(dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).isSelected()));
		
		dailyTaskCheckboxes.add(new JCheckBox("장바구니", status.isDailyBasketReward()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).addActionListener(e->status.setDailyBasketReward(dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).isSelected()));
		
		dailyTaskCheckboxes.add(new JCheckBox("특별패키지", status.isDailySpecialReward()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).addActionListener(e->status.setDailySpecialReward(dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).isSelected()));
		
		dailyTaskCheckboxes.add(new JCheckBox("사판훈련", status.isDailySandTraning()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).addActionListener(e->status.setDailySandTraning(dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).isSelected()));
		
		dailyTaskCheckboxes.add(new JCheckBox("일반&스킬모집", status.isDailyNormalIncrutAndSkill()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).addActionListener(e->status.setDailyNormalIncrutAndSkill(dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).isSelected()));
		
		dailyTaskCheckboxes.add(new JCheckBox("고급모집2회", status.isDailyAdvancedIncruit()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).addActionListener(e->status.setDailyAdvancedIncruit(dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).isSelected()));
		
		dailyTaskCheckboxes.add(new JCheckBox("일일업무", status.isDailyQuestReward()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).addActionListener(e->status.setDailyQuestReward(dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).isSelected()));
		
		dailyTaskCheckboxes.add(new JCheckBox("크로스패배10회", status.isDailyCrossBattle()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).addActionListener(e->status.setDailyCrossBattle(dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).isSelected()));
		
		dailyTaskCheckboxes.add(new JCheckBox("무료다이아20회", status.isDailyGemReward()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).addActionListener(e->status.setDailyGemReward(dailyTaskCheckboxes.get(dailyTaskCheckboxes.size()-1).isSelected()));
		
		for(JCheckBox checkbox : dailyTaskCheckboxes) {
			dailyTaskPanel.add(checkbox);
		}
		
		dailyPanel.add(dailyTaskPanel);
		
		JPanel dailyButtonPanel = new JPanel(new MigLayout("align right", "[]10[]", ""));
		taskCheckButton.setFont(buttonFont);
		taskCheckButton.addActionListener(e->{
			for(JCheckBox checkbox : dailyTaskCheckboxes) {
				checkbox.setSelected(true);
				for(ActionListener listener : checkbox.getActionListeners()) {
					listener.actionPerformed(new ActionEvent(checkbox, ActionEvent.ACTION_PERFORMED, ""));
				}
			}
		});
		dailyButtonPanel.add(taskCheckButton);
		
		taskReleaseButton.setFont(buttonFont);
		taskReleaseButton.addActionListener(e->{
			for(JCheckBox checkbox : dailyTaskCheckboxes) {
				checkbox.setSelected(false);
				for(ActionListener listener : checkbox.getActionListeners()) {
					listener.actionPerformed(new ActionEvent(checkbox, ActionEvent.ACTION_PERFORMED, ""));
				}
			}
		});
		dailyButtonPanel.add(taskReleaseButton);
		
		taskRunButton.setBackground(new Color(46, 204, 113));
		taskRunButton.setForeground(Color.white);
		taskRunButton.setFont(buttonFont);
		taskRunButton.addActionListener(e->{
			int count = 0;
			for(JCheckBox checkbox : dailyTaskCheckboxes) {
				if(checkbox.isSelected()) {
					count++;
				}
			}
			
			if(count == 0) {
				JOptionPane.showMessageDialog(this, "1개 이상의 작업을 선택하세요");
				return;
			}
			
			playTaskMacro();
		});
		
		dailyButtonPanel.add(taskRunButton);
		dailyPanel.add(dailyButtonPanel, "growx");
		
		contentPanel.add(dailyPanel);
	}
	
	public void events() throws Exception {
		setPlayingState(false);
		
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
		if(timelinesGroup.isPlaying()) return;
		Rectangle screenRect = ScreenRectDialog.showDialog(MainFrame.this);
		status.getScreenList().add(screenRect);
		screenCountLabel.setText("현재 설정된 화면 : " + status.getScreenList().size());
		setPlayingState(false);
	}
	private void removeScreenRect() {
		if(timelinesGroup.isPlaying()) return;
		if(status.getScreenList().isEmpty()) return;
		status.getScreenList().remove(status.getScreenList().size()-1);
		screenCountLabel.setText("현재 설정된 화면 : " + status.getScreenList().size());
		setPlayingState(false);
	}
	
	private void playDarkforceMacroOnce() {
		if(status.getScreenList().isEmpty()) return;
		if(timelinesGroup.isPlaying()) return;
		
		timelinesGroup.clear();
		MacroTimelines timelines = new MacroTimelines(true);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.암흑매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		timelinesGroup.add(timelines);
		timelinesGroup.playOnce();
		setPlayingState(true);
	}
	private void playDarkforceMacroLoop(int count) {
		if(count < 1) return;
		if(status.getScreenList().isEmpty()) return;
		if(timelinesGroup.isPlaying()) return;
		
		timelinesGroup.clear();
		
		MacroTimelines timelines = new MacroTimelines(true);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.암흑반복매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		timelinesGroup.add(timelines);
		timelinesGroup.play(count);
		setPlayingState(true);
	}
	private void playDarkforceMacroLoop() {
		if(status.getScreenList().isEmpty()) return;
		if(timelinesGroup.isPlaying()) return;
		
		timelinesGroup.clear();
		
		MacroTimelines timelines = new MacroTimelines(true);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.암흑반복매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		timelinesGroup.add(timelines);
		timelinesGroup.play();
		setPlayingState(true);
	}
	private void playWarhammerMacroOnce() {
		if(status.getScreenList().isEmpty()) return;
		if(timelinesGroup.isPlaying()) return;
		
		timelinesGroup.clear();
		
		MacroTimelines timelines = new MacroTimelines(true);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.워해머매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		timelinesGroup.add(timelines);
		timelinesGroup.playOnce();
		setPlayingState(true);
	}
	private void playWarhammerMacroLoop(int count) {
		if(count < 1) return;
		if(status.getScreenList().isEmpty()) return;
		if(timelinesGroup.isPlaying()) return;
		timelinesGroup.clear();
		MacroTimelines timelines = new MacroTimelines(true);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.워해머반복매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		timelinesGroup.add(timelines);
		timelinesGroup.play(count);
		setPlayingState(true);
	}
	private void playWarhammerMacroLoop() {
		if(status.getScreenList().isEmpty()) return;
		if(timelinesGroup.isPlaying()) return;
		timelinesGroup.clear();
		MacroTimelines timelines = new MacroTimelines(true);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.워해머반복매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		timelinesGroup.add(timelines);
		timelinesGroup.play();
		setPlayingState(true);
	}
	private void playTerror4kMacroOnce() {
		if(status.getScreenList().isEmpty()) return;
		if(timelinesGroup.isPlaying()) return;
		timelinesGroup.clear();
		MacroTimelines timelines = new MacroTimelines(false);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.테러매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		timelinesGroup.add(timelines);
		timelinesGroup.playOnce();
		setPlayingState(true);
	}
	private void playTerror4kMacroLoop(int count) {
		if(count < 1 || count > 10) return;
		if(status.getScreenList().isEmpty()) return;
		if(timelinesGroup.isPlaying()) return;
		timelinesGroup.clear();
		MacroTimelines timelines = new MacroTimelines(false);
		for(Rectangle screenRect : status.getScreenList()) {
			MacroTimeline timeline = MacroTimelineFactory.테러반복매크로(status, screenRect.getLocation());
			timelines.add(timeline);
		}
		timelinesGroup.add(timelines);
		timelinesGroup.play(count);
		setPlayingState(true);
	}
	private void stopMacro() {
		if(timelinesGroup.isPlaying()) {
			timelinesGroup.stop();
		}
	}
	private void playTaskMacro() {
		if(status.getScreenList().isEmpty()) return;
		if(timelinesGroup.isPlaying()) return;
		timelinesGroup.clear();
		
		if(status.isDailyVipReward()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.VIP보상받기매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isDailyBasketReward()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.장바구니매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isDailySpecialReward()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.패키지무료보상매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isDailyGemReward()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.무료보석수집매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isDailySandTraning()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.사판훈련매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isDailyNormalIncrutAndSkill()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.일반모집스킬모집매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isDailyCrossBattle()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.크로스패배매크로(status, screenRect.getLocation(), 10);
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isDailyAdvancedIncruit()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.고급모집2회매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isWeeklyDecorFreeToken()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.주간장식세트무료쿠폰매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isDailyQuestReward()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.일일임무매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isOilFacility()) {
			MacroTimelines timelines = new MacroTimelines(true);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.석유시설매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isFoodFacility()) {
			MacroTimelines timelines = new MacroTimelines(true);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.식량시설매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isOdinFacility()) {
			MacroTimelines timelines = new MacroTimelines(true);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.오딘시설매크로(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		if(status.isGoldRequest()) {
			MacroTimelines timelines = new MacroTimelines(false);
			for(Rectangle screenRect : status.getScreenList()) {
				MacroTimeline timeline = MacroTimelineFactory.골드지원요청(status, screenRect.getLocation());
				timelines.add(timeline);
			}
			timelinesGroup.add(timelines);
		}
		
		timelinesGroup.playOnce();
		setPlayingState(true);
	}
	
	private void setPlayingState(boolean isPlay) {
		if(status.getScreenList().isEmpty()) {
			areaButton.setEnabled(true);
			areaRemoveButton.setEnabled(false);
			darkforceOnceButton.setEnabled(false);
			darkforceTenButton.setEnabled(false);
			darkforceInputButton.setEnabled(false);
			darkforceLoopButton.setEnabled(false);
			warhammer1Button.setEnabled(false);
			warhammer10Button.setEnabled(false);
			warhammer15Button.setEnabled(false);
			warhammerCustomButton.setEnabled(false);
			warhammerLoopButton.setEnabled(false);
			terror4kOnceButton.setEnabled(false);
			terror4kFiveButton.setEnabled(false);
			terror4kLoopButton.setEnabled(false);
			macroStopButton.setEnabled(false);
			taskRunButton.setEnabled(false);
		}
		else {
			areaButton.setEnabled(isPlay == false);
			areaRemoveButton.setEnabled(isPlay == false);
			darkforceOnceButton.setEnabled(isPlay == false);
			darkforceTenButton.setEnabled(isPlay == false);
			darkforceInputButton.setEnabled(isPlay == false);
			darkforceLoopButton.setEnabled(isPlay == false);
			warhammer1Button.setEnabled(isPlay == false);
			warhammer10Button.setEnabled(isPlay == false);
			warhammer15Button.setEnabled(isPlay == false);
			warhammerCustomButton.setEnabled(isPlay == false);
			warhammerLoopButton.setEnabled(isPlay == false);
			macroStopButton.setEnabled(isPlay == true);
			terror4kOnceButton.setEnabled(isPlay == false);
			terror4kFiveButton.setEnabled(isPlay == false);
			terror4kLoopButton.setEnabled(isPlay == false);
			taskRunButton.setEnabled(isPlay == false);
		}
	}
	
}

