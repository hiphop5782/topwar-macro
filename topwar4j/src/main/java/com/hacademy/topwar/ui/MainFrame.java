package com.hacademy.topwar.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

import com.hacademy.topwar.constant.Delay;
import com.hacademy.topwar.macro.MacroCreator;
import com.hacademy.topwar.macro.MacroStatus;
import com.hacademy.topwar.macro.MacroTimelines;
import com.hacademy.topwar.macro.MacroTimelinesGroup;
import com.hacademy.topwar.macro.MacroTimelinesListener;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

@Getter
public class MainFrame extends JFrame {
	
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
	
	private List<JComponent> areaComponentList = new ArrayList<>();
	
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

	private CheckButton dailyTaskCheckButton = new CheckButton("일일 작업");
	private CheckButton weeklyTaskCheckButton = new CheckButton("주간 작업");
	private CheckButton trainingTaskCheckButton = new CheckButton("유닛 훈련");
	private CheckButton etcTaskCheckButton = new CheckButton("기타 작업");
	private CheckButton facilityTaskCheckButton = new CheckButton("시설 작업");
	private JButton taskRunButton = new JButton("작업 시작");
	private JButton smartRunButton = new JButton("요일을 고려하여 스마트 실행(Smart Run)");

	private List<JComponent> waitingComponentList = new ArrayList<>();
	private List<JComponent> runningComponentList = new ArrayList<>();
	
	private JScrollPane jsp = new JScrollPane();
	private JTextArea jtx = new JTextArea();
	
	private List<JComponent> maximizeComponents = new ArrayList<>();
	private List<JComponent> minimizeComponents = new ArrayList<>();
	private boolean mini = false;
	
	private List<JCheckBox> dailyTaskCheckboxes = new ArrayList<>();
	private List<JCheckBox> weeklyTaskCheckboxes = new ArrayList<>();
	private List<JCheckBox> etcTaskCheckboxes = new ArrayList<>();
	private List<JCheckBox> facilityTaskCheckboxes = new ArrayList<>();
	
	public MainFrame() throws Exception {
		WindowStatus status = WindowStatus.load();
		this.setAlwaysOnTop(false);
		if(status == null) 
			this.setLocationByPlatform(true);
		else 
			this.setLocation(status.getX(), status.getY());
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
		if(status != null) {
			this.setMinimode(status.isMini());
		}
	}
	
	public void setMinimode(boolean mini) {
		this.mini = mini;
		for(JComponent component : minimizeComponents) {
			component.setVisible(mini);
		}
		for(JComponent component : maximizeComponents) {
			component.setVisible(!mini);
		}
		this.revalidate();
		this.repaint();
		this.pack();
	}

	public void init() throws Exception {
		this.menu();
		this.components();
		this.events();
		
		for(JComponent component : waitingComponentList) {
			if(component instanceof JCheckBox checkbox) {
				for(ActionListener listener : checkbox.getActionListeners()) {
					listener.actionPerformed(new ActionEvent(checkbox, ActionEvent.ACTION_PERFORMED, ""));
				}
			}
		}
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

		JMenuItem openMacro = new JMenuItem("설정 열기");
		openMacro.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		openMacro.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
			int choose = chooser.showOpenDialog(this);
			if (choose == JFileChooser.APPROVE_OPTION) {
				status = MacroStatus.load(chooser.getSelectedFile());
				screenCountLabel.setText("현재 설정된 화면 : " + status.getScreenList().size());
			}
		});
		file.add(openMacro);

		JMenuItem saveMacro = new JMenuItem("설정 저장");
		saveMacro.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		saveMacro.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
			int choose = chooser.showSaveDialog(this);
			if (choose == JFileChooser.APPROVE_OPTION) {
				status.save(chooser.getSelectedFile());
			}
		});
		file.add(saveMacro);

//		file.addSeparator();

		JMenuItem exit = new JMenuItem("종료");
		exit.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
		exit.addActionListener(e -> {
			exitProgram();
		});
		file.add(exit);

		JMenu setting = new JMenu("설정");
		bar.add(setting);
		
		JMenuItem minimize = new JMenuItem("미니모드");
		minimize.setAccelerator(KeyStroke.getKeyStroke("F11"));
		minimize.addActionListener(e->setMinimode(true));
		setting.add(minimize);
		
		JMenuItem maximize = new JMenuItem("일반모드");
		maximize.setAccelerator(KeyStroke.getKeyStroke("F12"));
		maximize.addActionListener(e->setMinimode(false));
		setting.add(maximize);
		
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
		
		// 메인 패널
		JPanel mainPanel = new JPanel(new MigLayout("wrap 2, inset 0, hidemode 3", "[]5[]", ""));
		this.setContentPane(mainPanel);
		
		JPanel contentPanel = new JPanel(new MigLayout("wrap 1, insets 5, hidemode 3", "[grow,fill]", "[]3[]"));
		maximizeComponents.add(contentPanel);
		mainPanel.add(contentPanel);
		
		// 사용할 테두리
		Border lineBorder2 = BorderFactory.createLineBorder(Color.black, 2, true);
		Border lineBorder1 = BorderFactory.createLineBorder(Color.gray, 1, true);

		// 라벨 영역
		JPanel statusPanel = new JPanel(new GridLayout(2, 2));

		JButton areaButton = new JButton("영역설정(F2)");
		areaButton.setBackground(new Color(99, 110, 114));
		areaButton.setForeground(Color.white);
		areaButton.setFont(buttonFont);
		areaButton.addActionListener(e ->addScreenRect());
		waitingComponentList.add(areaButton);
		areaComponentList.add(areaButton);

		areaRemoveButton.setBackground(new Color(243, 156, 18));
		areaRemoveButton.setForeground(Color.white);
		areaRemoveButton.setFont(buttonFont);
		areaRemoveButton.addActionListener(e -> removeScreenRect());
		waitingComponentList.add(areaRemoveButton);
		
		screenCountLabel = new JLabel("현재 설정된 화면 : " + status.getScreenList().size(), JLabel.LEFT);
		macroExecuteCountLabel = new JLabel("매크로 실행 횟수 : 0", JLabel.LEFT);
		
		statusPanel.add(screenCountLabel);
		statusPanel.add(macroExecuteCountLabel);
		statusPanel.add(areaButton);
		statusPanel.add(areaRemoveButton);

		contentPanel.add(statusPanel);

		macroStopButton.setBackground(new Color(214, 48, 49));
		macroStopButton.setForeground(Color.white);
		macroStopButton.setFont(buttonFont);
		macroStopButton.addActionListener(e ->stopMacro());
		runningComponentList.add(macroStopButton);
		contentPanel.add(macroStopButton);
		
		// 부대번호
		JPanel darkforceMarchPanel = new JPanel(new MigLayout("", "[grow]10[grow]"));
		darkforceMarchPanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "부대번호 설정"));

		ButtonGroup darkforceMarchGroup = new ButtonGroup();
		for (int i = 1; i <= 8; i++) {
			JRadioButton radio = new JRadioButton(i + "번", i == status.getDarkforceMarchNumber());
			radio.addActionListener(e -> {
				String text = radio.getText();
				int number = Integer.parseInt(text.substring(0, 1));
				status.setDarkforceMarchNumber(number);
			});
			darkforceMarchPanel.add(radio);
			darkforceMarchGroup.add(radio);
			waitingComponentList.add(radio);
		}
		contentPanel.add(darkforceMarchPanel);

		// 물약사용 체크박스
		JPanel useVitPanel = new JPanel(new BorderLayout());

		JCheckBox useVit = new JCheckBox("물약 사용", status.isPotion());
		useVit.addActionListener(e -> {
			status.setPotion(useVit.isSelected());
		});

		useVitPanel.add(useVit);
		contentPanel.add(useVitPanel);
		waitingComponentList.add(useVit);

		// 암흑사냥
		JPanel darkforcePanel = new JPanel(new MigLayout("inset 5", "[]10[grow,fill]", ""));
		darkforcePanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "암흑(DarkForce)"));

		// [암흑사냥] 횟수
		JPanel darkforceCountPanel = new JPanel(new MigLayout("", "[]10[]", ""));
		darkforceCountPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "횟수"));
		
		JRadioButton darkforceCount1 = new JRadioButton("1회", status.getDarkforceAttackCount() == 1);
		JRadioButton darkforceCount2 = new JRadioButton("5회", status.getDarkforceAttackCount() == 5);
		darkforceCount1.addActionListener(e -> status.setDarkforceAttackCount(1));
		darkforceCount2.addActionListener(e -> status.setDarkforceAttackCount(5));
		darkforceCountPanel.add(darkforceCount1);
		darkforceCountPanel.add(darkforceCount2);
		ButtonGroup darkforceCountGroup = new ButtonGroup();
		darkforceCountGroup.add(darkforceCount1);
		darkforceCountGroup.add(darkforceCount2);
		waitingComponentList.add(darkforceCount1);
		waitingComponentList.add(darkforceCount2);
		
		// [암흑사냥] 레벨 - 영땅용
		JPanel darkforceLevelPanel = new JPanel(new MigLayout("", "[]10[]", ""));
		darkforceLevelPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "레벨(영땅용)"));
		
		JComboBox<String> darkforceLevelBox = new JComboBox<>(new String[] {"random", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"});
		darkforceLevelBox.setSelectedItem(status.getDarkforceLevel());
		darkforceLevelBox.addActionListener(e->status.setDarkforceLevel((String) darkforceLevelBox.getSelectedItem()));
		waitingComponentList.add(darkforceLevelBox);
		darkforceLevelPanel.add(darkforceLevelBox);
		
		// [암흑사냥] 시간 - 영땅용
		JPanel darkforceDurationPanel = new JPanel(new MigLayout("", "", ""));
		darkforceDurationPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "후딜레이(초)"));
		
		JTextField durationField = new JTextField();
		((AbstractDocument)durationField.getDocument()).setDocumentFilter(new DocumentFilter() {
			public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
				if(string.matches("\\d+")) {
					super.insertString(fb, offset, string, attr);
				}
			};
			public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
				if(text.matches("\\d+")) {
					super.replace(fb, offset, length, text, attrs);
				}
			};
		});
		durationField.setText(String.valueOf(status.getDarkforceDuration()));
		durationField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				status.setDarkforceDuration(Integer.parseInt(durationField.getText()));
			}
		});
		darkforceDurationPanel.add(durationField);
		waitingComponentList.add(durationField);
		
		// [암흑사냥] 실행버튼
		JPanel darkforceButtonPanel = new JPanel(new MigLayout("wrap 2, align right", "[]10[]", ""));

		darkforceOnceButton.setBackground(new Color(46, 204, 113));
		darkforceOnceButton.setForeground(Color.white);
		darkforceOnceButton.setFont(buttonFont);
		darkforceOnceButton.addActionListener(e -> {
			try {
				playDarkforceMacroOnce();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		darkforceButtonPanel.add(darkforceOnceButton);

		darkforceTenButton.setBackground(new Color(46, 204, 113));
		darkforceTenButton.setForeground(Color.white);
		darkforceTenButton.setFont(buttonFont);
		darkforceTenButton.addActionListener(e -> {
			try {
				playDarkforceMacroLoop(10);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		darkforceButtonPanel.add(darkforceTenButton);

		darkforceInputButton.setBackground(new Color(42, 52, 54));
		darkforceInputButton.setForeground(Color.white);
		darkforceInputButton.setFont(buttonFont);
		darkforceInputButton.addActionListener(e -> {
			try {
				String input = JOptionPane.showInputDialog(MainFrame.this, "횟수 입력");
				int count = Integer.parseInt(input);
				if (count > 0) {
					playDarkforceMacroLoop(count);
				}
			} catch (Exception ex) {
			}
		});
		darkforceButtonPanel.add(darkforceInputButton);
		
		darkforceLoopButton.setBackground(new Color(9, 132, 227));
		darkforceLoopButton.setForeground(Color.white);
		darkforceLoopButton.setFont(buttonFont);
		darkforceLoopButton.addActionListener(e -> {
			try {
				playDarkforceMacroLoop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		darkforceButtonPanel.add(darkforceLoopButton);

		darkforcePanel.add(darkforceCountPanel);
		darkforcePanel.add(darkforceLevelPanel);
		darkforcePanel.add(darkforceDurationPanel);
		darkforcePanel.add(darkforceButtonPanel);

		waitingComponentList.add(darkforceOnceButton);
		waitingComponentList.add(darkforceTenButton);
		waitingComponentList.add(darkforceInputButton);
		waitingComponentList.add(darkforceLoopButton);

		// [암흑사냥] 최종추가
		contentPanel.add(darkforcePanel);

		// [워해머4k]
		JPanel warhammer4kPanel = new JPanel(new MigLayout("inset 10, align right", "[]10[]", ""));
		warhammer4kPanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "워해머(Warhammer-4k)"));

		warhammer1Button.setBackground(new Color(46, 204, 113));
		warhammer1Button.setForeground(Color.white);
		warhammer1Button.setFont(buttonFont);
		warhammer1Button.addActionListener(e -> {
			try {
				playWarhammerMacroOnce();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		warhammer4kPanel.add(warhammer1Button);

		warhammer10Button.setBackground(new Color(46, 204, 113));
		warhammer10Button.setForeground(Color.white);
		warhammer10Button.setFont(buttonFont);
		warhammer10Button.addActionListener(e -> {
			try {
				playWarhammerMacroLoop(10);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		warhammer4kPanel.add(warhammer10Button);

		warhammer15Button.setBackground(new Color(46, 204, 113));
		warhammer15Button.setForeground(Color.white);
		warhammer15Button.setFont(buttonFont);
		warhammer15Button.addActionListener(e -> {
			try {
				playWarhammerMacroLoop(15);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		warhammer4kPanel.add(warhammer15Button);

		warhammerCustomButton.setBackground(new Color(42, 52, 54));
		warhammerCustomButton.setForeground(Color.white);
		warhammerCustomButton.setFont(buttonFont);
		warhammerCustomButton.addActionListener(e -> {
			try {
				String input = JOptionPane.showInputDialog(MainFrame.this, "횟수 입력");
				int count = Integer.parseInt(input);
				if (count > 0) {
					playWarhammerMacroLoop(count);
				}
			} catch (Exception ex) {
			}
		});
		warhammer4kPanel.add(warhammerCustomButton);
		
		warhammerLoopButton.setBackground(new Color(9, 132, 227));
		warhammerLoopButton.setForeground(Color.white);
		warhammerLoopButton.setFont(buttonFont);
		warhammerLoopButton.addActionListener(e -> {
			try {
				playWarhammerMacroLoop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		warhammer4kPanel.add(warhammerLoopButton);

		waitingComponentList.add(warhammer1Button);
		waitingComponentList.add(warhammer10Button);
		waitingComponentList.add(warhammer15Button);
		waitingComponentList.add(warhammerCustomButton);
		waitingComponentList.add(warhammerLoopButton);

		// [워해머4k] 최종추가
		contentPanel.add(warhammer4kPanel);

		// [테러4k]
		JPanel terror4kPanel = new JPanel(new MigLayout("inset 5", "[]10[]10[grow,fill]", ""));
		terror4kPanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "테러(Terror-4k)"));

		// [테러4k] 테러레벨 선택
		JPanel terror4kLevelPanel = new JPanel(new MigLayout("inset 2", "[]5[]", ""));
		terror4kLevelPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "레벨"));

		ButtonGroup terror4kLevelGroup = new ButtonGroup();
		for (int i = 1; i <= 5; i++) {
			JRadioButton radio = new JRadioButton(String.valueOf(i), i == status.getTerror4kLevel());
			radio.addActionListener(e -> {
				status.setTerror4kLevel(Integer.parseInt(radio.getText().substring(0, 1)));
			});
			terror4kLevelGroup.add(radio);
			terror4kLevelPanel.add(radio);
			waitingComponentList.add(radio);
		}

		terror4kPanel.add(terror4kLevelPanel);

		// [테러4k] 집결/공격 선택
		JPanel terror4kAttackTypePanel = new JPanel(new MigLayout("inset 2", "[]5[]", ""));
		terror4kAttackTypePanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "방식"));
		ButtonGroup terror4kAttackTypeGroup = new ButtonGroup();
		JRadioButton terror4kAttackRally = new JRadioButton("집결", status.isTerror4kManual() == false);
		JRadioButton terror4kAttackManual = new JRadioButton("공격", status.isTerror4kManual() == true);
		terror4kAttackRally.addActionListener(e -> {
			status.setTerror4kManual(false);
		});
		terror4kAttackManual.addActionListener(e -> {
			status.setTerror4kManual(true);
		});

		terror4kAttackTypePanel.add(terror4kAttackRally);
		terror4kAttackTypePanel.add(terror4kAttackManual);
		terror4kAttackTypeGroup.add(terror4kAttackRally);
		terror4kAttackTypeGroup.add(terror4kAttackManual);

		waitingComponentList.add(terror4kAttackRally);
		waitingComponentList.add(terror4kAttackManual);

		terror4kPanel.add(terror4kAttackTypePanel);

		// 테러 공격/중지 버튼
		JPanel terror4kButtonPanel = new JPanel(new MigLayout("inset 5, align right", "[]10[]", ""));

		terror4kOnceButton.setBackground(new Color(46, 204, 113));
		terror4kOnceButton.setForeground(Color.white);
		terror4kOnceButton.setFont(buttonFont);
		terror4kOnceButton.addActionListener(e -> {
			try {
				playTerror4kMacroOnce();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		terror4kFiveButton.setBackground(new Color(46, 204, 113));
		terror4kFiveButton.setForeground(Color.white);
		terror4kFiveButton.setFont(buttonFont);
		terror4kFiveButton.addActionListener(e -> {
			try {
				playTerror4kMacroLoop(5);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		terror4kLoopButton.setBackground(new Color(9, 132, 227));
		terror4kLoopButton.setForeground(Color.white);
		terror4kLoopButton.setFont(buttonFont);
		terror4kLoopButton.addActionListener(e -> {
			try {
				playTerror4kMacroLoop(10);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		terror4kButtonPanel.add(terror4kOnceButton);
		terror4kButtonPanel.add(terror4kFiveButton);
		terror4kButtonPanel.add(terror4kLoopButton);
		terror4kPanel.add(terror4kButtonPanel);

		waitingComponentList.add(terror4kOnceButton);
		waitingComponentList.add(terror4kFiveButton);
		waitingComponentList.add(terror4kLoopButton);

		contentPanel.add(terror4kPanel);
		
		JPanel taskPanel = new JPanel(new MigLayout("wrap 1", "[grow]", ""));
		taskPanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "반복 작업"));

		JPanel dailyTaskPanel = new JPanel(new MigLayout("wrap 5", "[][][][][]", ""));
		dailyTaskPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "일일 작업"));

		
		dailyTaskCheckboxes.add(new JCheckBox("VIP보상", status.isDailyVipReward()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size() - 1).addActionListener(e -> {
			JCheckBox checkbox = (JCheckBox) e.getSource();
			status.setDailyVipReward(checkbox.isSelected());
			dailyTaskCheckButton.refreshChecked();
		});

		dailyTaskCheckboxes.add(new JCheckBox("장바구니", status.isDailyBasketReward()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size() - 1).addActionListener(e -> {
			JCheckBox checkbox = (JCheckBox) e.getSource();
			status.setDailyBasketReward(checkbox.isSelected());
			dailyTaskCheckButton.refreshChecked();
		});

		dailyTaskCheckboxes.add(new JCheckBox("특별패키지", status.isDailySpecialReward()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size() - 1).addActionListener(e -> {
			JCheckBox checkbox = (JCheckBox) e.getSource();
			status.setDailySpecialReward(checkbox.isSelected());
			dailyTaskCheckButton.refreshChecked();
		});

		dailyTaskCheckboxes.add(new JCheckBox("사판훈련", status.isDailySandTraning()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size() - 1).addActionListener(e -> {
			JCheckBox checkbox = (JCheckBox) e.getSource();
			status.setDailySandTraning(checkbox.isSelected());
			dailyTaskCheckButton.refreshChecked();
		});

		dailyTaskCheckboxes.add(new JCheckBox("일반&스킬모집", status.isDailyNormalIncrutAndSkill()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size() - 1).addActionListener(e -> {
			JCheckBox checkbox = (JCheckBox) e.getSource();
			status.setDailyNormalIncrutAndSkill(checkbox.isSelected());
			dailyTaskCheckButton.refreshChecked();
		});

		dailyTaskCheckboxes.add(new JCheckBox("고급모집(2회)", status.isDailyAdvancedIncruit()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size() - 1).addActionListener(e -> {
			JCheckBox checkbox = (JCheckBox) e.getSource();
			status.setDailyAdvancedIncruit(checkbox.isSelected());
			dailyTaskCheckButton.refreshChecked();
		});

		dailyTaskCheckboxes.add(new JCheckBox("일일업무", status.isDailyQuestReward()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size() - 1).addActionListener(e -> {
			JCheckBox checkbox = (JCheckBox) e.getSource();
			status.setDailyQuestReward(checkbox.isSelected());
			dailyTaskCheckButton.refreshChecked();
		});

//		dailyTaskCheckboxes.add(new JCheckBox("골드지원", status.isGoldRequest()));
//		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size() - 1).addActionListener(e -> {
//			JCheckBox checkbox = (JCheckBox) e.getSource();
//			status.setGoldRequest(checkbox.isSelected());
//		});

		dailyTaskCheckboxes.add(new JCheckBox("크로스패배10회", status.isDailyCrossBattle()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size() - 1).addActionListener(e -> {
			JCheckBox checkbox = (JCheckBox) e.getSource();
			status.setDailyCrossBattle(checkbox.isSelected());
			dailyTaskCheckButton.refreshChecked();
		});

		dailyTaskCheckboxes.add(new JCheckBox("무료다이아20회", status.isDailyGemReward()));
		dailyTaskCheckboxes.get(dailyTaskCheckboxes.size() - 1).addActionListener(e -> {
			JCheckBox checkbox = (JCheckBox) e.getSource();
			status.setDailyGemReward(checkbox.isSelected());
			dailyTaskCheckButton.refreshChecked();
		});

		for (JCheckBox checkbox : dailyTaskCheckboxes) {
			dailyTaskPanel.add(checkbox);
			waitingComponentList.add(checkbox);
		}

		taskPanel.add(dailyTaskPanel, "grow");

		JPanel rowPanel = new JPanel(new MigLayout("inset 0", "[fill,grow][fill,grow]", ""));

		// 주간퀘스트
		JPanel weeklyTaskPanel = new JPanel(new MigLayout("", "[]", ""));
		weeklyTaskPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "주간 작업"));

		weeklyTaskCheckboxes.add(new JCheckBox("무료장식토큰", status.isWeeklyDecorFreeToken()));
		weeklyTaskCheckboxes.get(weeklyTaskCheckboxes.size() - 1).addActionListener(e -> {
			JCheckBox checkbox = (JCheckBox) e.getSource();
			status.setWeeklyDecorFreeToken(checkbox.isSelected());
			weeklyTaskCheckButton.refreshChecked();
		});
		for (JCheckBox checkbox : weeklyTaskCheckboxes) {
			weeklyTaskPanel.add(checkbox);
			waitingComponentList.add(checkbox);
		}

		rowPanel.add(weeklyTaskPanel);
		
		// 기타 작업
		JPanel etcTaskPanel = new JPanel(new MigLayout("wrap 3", "[]10[]10[]", ""));
		etcTaskPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "기타 작업"));
		
		JPanel allianceDonationPanel = new JPanel(new MigLayout("inset 0", "", ""));
		JCheckBox allianceDonationCheckbox = new JCheckBox("길드기부10회", status.isAllianceDonation()); 
		allianceDonationCheckbox.addActionListener(e->{
			JCheckBox checkbox = (JCheckBox) e.getSource();
			status.setAllianceDonation(checkbox.isSelected());
			etcTaskCheckButton.refreshChecked();
		});
		allianceDonationPanel.add(allianceDonationCheckbox);
		etcTaskPanel.add(allianceDonationPanel);
		
		JPanel materialPanel = new JPanel(new MigLayout("inset 0", "[grow][]", ""));
		JCheckBox materialCheckbox = new JCheckBox("재료생산", status.isProductMaterial());
		JComboBox<String> materialTypebox = new JComboBox<>(new String[] {"강철","나사","트랜지스터", "고무", "텅스텐", "배터리", "유리"});
		materialCheckbox.addActionListener(e->{
			JCheckBox checkbox = (JCheckBox) e.getSource();
			status.setProductMaterial(checkbox.isSelected());
			materialTypebox.setEnabled(checkbox.isSelected());
			etcTaskCheckButton.refreshChecked();
		});
		materialTypebox.addActionListener(e->{
			status.setProductMaterialType((String)materialTypebox.getSelectedItem());
		});
		waitingComponentList.add(materialTypebox);
		
		materialPanel.add(materialCheckbox);
		materialPanel.add(materialTypebox);
		
		etcTaskPanel.add(materialPanel);
		
		rowPanel.add(etcTaskPanel);
		taskPanel.add(rowPanel, "grow");

		rowPanel = new JPanel(new MigLayout("inset 0", "[fill,grow]", ""));
		
		// 시설
		JPanel facilityTaskPanel = new JPanel(new MigLayout("", "[grow]5[grow]", ""));
//		facilityTaskPanel.setBackground(Color.yellow);
//		rowPanel.setBackground(Color.green);
		facilityTaskPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "자원 시설"));
		
		JPanel oilTaskPanel = new JPanel(new MigLayout("inset 0", "[grow][]", ""));
		JCheckBox oilTaskCheckbox = new JCheckBox("석유시설", status.isOilFacility());
		JComboBox<Integer> oilTaskLevel = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5 });
		oilTaskLevel.setSelectedItem(status.getOilFacilityLevel());

		oilTaskCheckbox.addActionListener(e -> {
			status.setOilFacility(oilTaskCheckbox.isSelected());
			oilTaskLevel.setEnabled(oilTaskCheckbox.isSelected());
			facilityTaskCheckButton.refreshChecked();
		});
		oilTaskLevel.addActionListener(e -> {
			status.setOilFacilityLevel((int) oilTaskLevel.getSelectedItem());
		});

		oilTaskPanel.add(oilTaskCheckbox);
		oilTaskPanel.add(oilTaskLevel);

		facilityTaskPanel.add(oilTaskPanel);

		JPanel foodTaskPanel = new JPanel(new MigLayout("inset 0", "[grow][]", ""));
		JCheckBox foodTaskCheckbox = new JCheckBox("식량시설", status.isFoodFacility());
		JComboBox<Integer> foodTaskLevel = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5 });
		foodTaskLevel.setSelectedItem(status.getFoodFacilityLevel());

		foodTaskCheckbox.addActionListener(e -> {
			status.setFoodFacility(foodTaskCheckbox.isSelected());
			foodTaskLevel.setEnabled(foodTaskCheckbox.isSelected());
			facilityTaskCheckButton.refreshChecked();
		});
		foodTaskLevel.addActionListener(e -> {
			status.setFoodFacilityLevel((int) foodTaskLevel.getSelectedItem());
		});

		foodTaskPanel.add(foodTaskCheckbox);
		foodTaskPanel.add(foodTaskLevel);

		facilityTaskPanel.add(foodTaskPanel);

		JPanel odinTaskPanel = new JPanel(new MigLayout("inset 0", "[grow][]", ""));
		JCheckBox odinTaskCheckbox = new JCheckBox("오딘시설", status.isOdinFacility());
		JComboBox<Integer> odinTaskLevel = new JComboBox<>(new Integer[] { 1, 2, 3 });
		odinTaskLevel.setSelectedItem(status.getOdinFacilityLevel());
		odinTaskCheckbox.addActionListener(e -> {
			status.setOdinFacility(odinTaskCheckbox.isSelected());
			odinTaskLevel.setEnabled(odinTaskCheckbox.isSelected());
			facilityTaskCheckButton.refreshChecked();
		});
		odinTaskLevel.addActionListener(e -> {
			status.setOdinFacilityLevel((int) odinTaskLevel.getSelectedItem());
		});

		odinTaskPanel.add(odinTaskCheckbox);
		odinTaskPanel.add(odinTaskLevel);

		facilityTaskPanel.add(odinTaskPanel);
		
		rowPanel.add(facilityTaskPanel, "grow");

		taskPanel.add(rowPanel, "grow");

		facilityTaskCheckboxes.add(oilTaskCheckbox);
		facilityTaskCheckboxes.add(foodTaskCheckbox);
		facilityTaskCheckboxes.add(odinTaskCheckbox);
		
		etcTaskCheckboxes.add(allianceDonationCheckbox);
		etcTaskCheckboxes.add(materialCheckbox);
		
		waitingComponentList.add(oilTaskLevel);
		waitingComponentList.add(foodTaskLevel);
		waitingComponentList.add(odinTaskLevel);
		
		for(JCheckBox checkbox : etcTaskCheckboxes) {
			waitingComponentList.add(checkbox);
		}

		JPanel taskButtonPanel = new JPanel(new MigLayout("align right", "[]10[]", ""));

		dailyTaskCheckButton.addActionListener(e->{
			CheckButton btn = (CheckButton)e.getSource();
			boolean current = btn.isChecked();
			btn.setChecked(!current);
		});
		dailyTaskCheckButton.setConcernedCheckboxes(dailyTaskCheckboxes);
		taskButtonPanel.add(dailyTaskCheckButton);

		weeklyTaskCheckButton.addActionListener(e -> {
			CheckButton btn = (CheckButton)e.getSource();
			boolean current = btn.isChecked();
			btn.setChecked(!current);
		});
		weeklyTaskCheckButton.setConcernedCheckboxes(weeklyTaskCheckboxes);
		taskButtonPanel.add(weeklyTaskCheckButton);

		etcTaskCheckButton.addActionListener(e -> {
			CheckButton btn = (CheckButton)e.getSource();
			boolean current = btn.isChecked();
			btn.setChecked(!current);
		});
		etcTaskCheckButton.setConcernedCheckboxes(etcTaskCheckboxes);
		taskButtonPanel.add(etcTaskCheckButton);
		
		facilityTaskCheckButton.addActionListener(e -> {
			CheckButton btn = (CheckButton)e.getSource();
			boolean current = btn.isChecked();
			btn.setChecked(!current);
		});
		facilityTaskCheckButton.setConcernedCheckboxes(facilityTaskCheckboxes);
		taskButtonPanel.add(facilityTaskCheckButton);

		taskRunButton.setBackground(new Color(46, 204, 113));
		taskRunButton.setForeground(Color.white);
		taskRunButton.setFont(buttonFont);
		taskRunButton.addActionListener(e -> {
			int count = 0;
			for (JCheckBox checkbox : dailyTaskCheckboxes) {
				if (checkbox.isSelected()) {
					count++;
				}
			}
			for (JCheckBox checkbox : weeklyTaskCheckboxes) {
				if (checkbox.isSelected()) {
					count++;
				}
			}
//			for (JCheckBox checkbox : trainingTaskCheckboxes) {
//				if (checkbox.isSelected()) {
//					count++;
//				}
//			}
			for (JCheckBox checkbox : etcTaskCheckboxes) {
				if (checkbox.isSelected()) {
					count++;
				}
			}

			if (count == 0) {
				JOptionPane.showMessageDialog(this, "1개 이상의 작업을 선택하세요");
				return;
			}

			try {
				playTaskMacro();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		taskButtonPanel.add(taskRunButton);
		taskPanel.add(taskButtonPanel, "growx");
		
		ActionListener smartRunTask = e -> {
			int count = 0;
			for (JCheckBox checkbox : dailyTaskCheckboxes) {
				if (checkbox.isSelected()) {
					count++;
				}
			}
			for (JCheckBox checkbox : weeklyTaskCheckboxes) {
				if (checkbox.isSelected()) {
					count++;
				}
			}
//			for (JCheckBox checkbox : trainingTaskCheckboxes) {
//				if (checkbox.isSelected()) {
//					count++;
//				}
//			}
			for (JCheckBox checkbox : etcTaskCheckboxes) {
				if (checkbox.isSelected()) {
					count++;
				}
			}

			if (count == 0) {
				JOptionPane.showMessageDialog(this, "1개 이상의 작업을 선택하세요");
				return;
			}

			try {
				playSmartTaskMacro();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		};

		JPanel smartButtonPanel = new JPanel(new MigLayout("align right", "[]"));
		smartRunButton.setBackground(new Color(9, 132, 227));
		smartRunButton.setForeground(Color.white);
		smartRunButton.setFont(buttonFont);
		smartRunButton.addActionListener(smartRunTask);

		smartButtonPanel.add(smartRunButton, "grow");
		taskPanel.add(smartButtonPanel, "grow");

		contentPanel.add(taskPanel);

		waitingComponentList.add(dailyTaskCheckButton);
		waitingComponentList.add(weeklyTaskCheckButton);
//		waitingComponentList.add(trainingTaskCheckButton);
		waitingComponentList.add(etcTaskCheckButton);
		waitingComponentList.add(taskRunButton);
		waitingComponentList.add(smartRunButton);
		
		// 로그 패널
		JPanel logPanel = new JPanel(new MigLayout("inset 5, fill"));
		logPanel.setPreferredSize(new Dimension(250, 0));
		jsp.setViewportView(jtx);
		logPanel.add(jsp, "grow, push");
		jtx.setFocusable(false);
		//mainPanel.add(logPanel, "grow, push");
		
		
		//최소화 패널
		JPanel minimizePanel = new JPanel(new MigLayout("insets 5, hidemode 3", "[grow]".repeat(7), ""));
		minimizeComponents.add(minimizePanel);
		mainPanel.add(minimizePanel);
		minimizePanel.setVisible(false);
		
		JButton areaButton2 = new JButton("영역추가");
		areaButton2.setBackground(areaButton.getBackground());
		areaButton2.setForeground(areaButton.getForeground());
		areaButton2.setFont(areaButton.getFont());
		areaButton2.addActionListener(e->addScreenRect());
		waitingComponentList.add(areaButton2);
		areaComponentList.add(areaButton2);
		minimizePanel.add(areaButton2);
		
		JButton areaRemoveButton2 = new JButton("영역제거");
		areaRemoveButton2.setBackground(new Color(243, 156, 18));
		areaRemoveButton2.setForeground(Color.white);
		areaRemoveButton2.setFont(buttonFont);
		areaRemoveButton2.addActionListener(e -> removeScreenRect());
		waitingComponentList.add(areaRemoveButton2);
		minimizePanel.add(areaRemoveButton2);
		
		JButton darkforceInputButton2 = new JButton("암흑");
		darkforceInputButton2.setBackground(darkforceInputButton.getBackground());
		darkforceInputButton2.setForeground(darkforceInputButton.getForeground());
		darkforceInputButton2.setFont(darkforceInputButton.getFont());
		darkforceInputButton2.addActionListener(e -> {
			try {
				playDarkforceMacroLoop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		minimizePanel.add(darkforceInputButton2);
		waitingComponentList.add(darkforceInputButton2);
		
		JButton warhammerCustomButton2 = new JButton("워해머");
		warhammerCustomButton2.setBackground(warhammerCustomButton.getBackground());
		warhammerCustomButton2.setForeground(warhammerCustomButton.getForeground());
		warhammerCustomButton2.setFont(warhammerCustomButton.getFont());
		warhammerCustomButton2.addActionListener(e -> {
			try {
				playWarhammerMacroLoop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		minimizePanel.add(warhammerCustomButton2);
		waitingComponentList.add(warhammerCustomButton2);
		
		JButton smartRunButton2 = new JButton("일일업무");
		smartRunButton2.setBackground(smartRunButton.getBackground());
		smartRunButton2.setForeground(smartRunButton.getForeground());
		smartRunButton2.setFont(smartRunButton.getFont());
		smartRunButton2.addActionListener(smartRunTask);
		minimizePanel.add(smartRunButton2);
		waitingComponentList.add(smartRunButton2);

		JButton terror4kLoopButton2 = new JButton("테러");
		terror4kLoopButton2.setBackground(terror4kLoopButton.getBackground());
		terror4kLoopButton2.setForeground(terror4kLoopButton.getForeground());
		terror4kLoopButton2.setFont(terror4kLoopButton.getFont());
		terror4kLoopButton2.addActionListener(e -> {
			try {
				playTerror4kMacroLoop(10);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		minimizePanel.add(terror4kLoopButton2);
		waitingComponentList.add(terror4kLoopButton2);

		
		JButton macroStopButton2 = new JButton("실행중지");
		macroStopButton2.setBackground(macroStopButton.getBackground());
		macroStopButton2.setForeground(macroStopButton.getForeground());
		macroStopButton2.setFont(macroStopButton.getFont());
		macroStopButton2.addActionListener(e -> stopMacro());
		runningComponentList.add(macroStopButton2);
		minimizePanel.add(macroStopButton2);
	}

	public void events() throws Exception {
		setPlayingState(false);

		// global keyboard listener
		GlobalKeyboardHook hook = new GlobalKeyboardHook(false);
		hook.addKeyListener(new GlobalKeyAdapter() {
			@Override
			public void keyReleased(GlobalKeyEvent event) {
				switch (event.getVirtualKeyCode()) {
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

	// 상태 저장 및 프로그램 종료
	public void exitProgram() {
		status.save();
		WindowStatus.save(this);
		System.exit(0);
	}

	private void addScreenRect() {
		if (timelinesGroup.isPlaying())
			return;
		Rectangle screenRect = ScreenRectDialog.showDialog(MainFrame.this);
		status.getScreenList().add(screenRect);
		screenCountLabel.setText("현재 설정된 화면 : " + status.getScreenList().size());
		setPlayingState(false);
	}

	private void removeScreenRect() {
		if (timelinesGroup.isPlaying())
			return;
		if (status.getScreenList().isEmpty())
			return;
		status.getScreenList().remove(status.getScreenList().size() - 1);
		screenCountLabel.setText("현재 설정된 화면 : " + status.getScreenList().size());
		setPlayingState(false);
	}

	private void playDarkforceMacroOnce() throws Exception {
		if (status.getScreenList().isEmpty())
			return;
		if (timelinesGroup.isPlaying())
			return;
		timelinesGroup.clear();
		
		MacroTimelines timelines = MacroCreator.darkforce(status);
		timelinesGroup.add(timelines);
		timelinesGroup.playOnce();
		setPlayingState(true);
	}

	private void playDarkforceMacroLoop(int count) throws Exception {
		if (count < 1)
			return;
		if (status.getScreenList().isEmpty())
			return;
		if (timelinesGroup.isPlaying())
			return;
		timelinesGroup.clear();

		MacroTimelines timelines = MacroCreator.darkforceLoop(status);
		timelinesGroup.add(timelines);
		timelinesGroup.play(count);
		setPlayingState(true);
	}

	private void playDarkforceMacroLoop() throws Exception {
		if (status.getScreenList().isEmpty())
			return;
		if (timelinesGroup.isPlaying())
			return;
		timelinesGroup.clear();

		MacroTimelines timelines = MacroCreator.darkforceLoop(status);
		timelinesGroup.add(timelines);
		timelinesGroup.play();
		setPlayingState(true);
	}

	private void playWarhammerMacroOnce() throws Exception {
		if (status.getScreenList().isEmpty())
			return;
		if (timelinesGroup.isPlaying())
			return;

		timelinesGroup.clear();

		MacroTimelines timelines = MacroCreator.warhammer4k(status);
		timelinesGroup.add(timelines);
		timelinesGroup.playOnce();
		setPlayingState(true);
	}

	private void playWarhammerMacroLoop(int count) throws Exception {
		if (count < 1)
			return;
		if (status.getScreenList().isEmpty())
			return;
		if (timelinesGroup.isPlaying())
			return;
		timelinesGroup.clear();
		
		MacroTimelines timelines = MacroCreator.warhammer4k(status, 100);
		timelinesGroup.add(timelines);
		timelinesGroup.play(count);
		setPlayingState(true);
	}

	private void playWarhammerMacroLoop() throws Exception {
		if (status.getScreenList().isEmpty())
			return;
		if (timelinesGroup.isPlaying())
			return;
		timelinesGroup.clear();
		
		MacroTimelines timelines = MacroCreator.warhammer4k(status, 100);
		timelinesGroup.add(timelines);
		timelinesGroup.play();
		setPlayingState(true);
	}

	private void playTerror4kMacroOnce() throws Exception {
		if (status.getScreenList().isEmpty())
			return;
		if (timelinesGroup.isPlaying())
			return;
		timelinesGroup.clear();
		
		MacroTimelines timelines = MacroCreator.terror4k(status);
		timelinesGroup.add(timelines);
		timelinesGroup.playOnce();
		setPlayingState(true);
	}

	private void playTerror4kMacroLoop(int count) throws Exception {
		if (count < 1 || count > 10)
			return;
		if (status.getScreenList().isEmpty())
			return;
		if (timelinesGroup.isPlaying())
			return;
		timelinesGroup.clear();

		MacroTimelines timelines = MacroCreator.terror4k(status, Delay.TERROR.getDuration());
		timelinesGroup.add(timelines);
		timelinesGroup.play(count);
		setPlayingState(true);
	}

	private void stopMacro() {
		if (timelinesGroup.isPlaying()) {
			timelinesGroup.stop();
		}
	}

	private void playTaskMacro() throws Exception {
		if (status.getScreenList().isEmpty())
			return;
		if (timelinesGroup.isPlaying())
			return;
		MacroCreator.task(timelinesGroup, status);
		timelinesGroup.playOnce();
		setPlayingState(true);
	}

	private void playSmartTaskMacro() throws Exception {
		if (status.getScreenList().isEmpty())
			return;
		if (timelinesGroup.isPlaying())
			return;
		MacroCreator.task(timelinesGroup, status, true);
		timelinesGroup.playOnce();
		setPlayingState(true);
	}

	private void setPlayingState(boolean isPlay) {
		if (status.getScreenList().isEmpty()) {
			for (JComponent component : waitingComponentList) {
				component.setEnabled(false);
			}
			for (JComponent component : runningComponentList) {
				component.setEnabled(false);
			}
			for(JComponent component : areaComponentList) {
				component.setEnabled(true);
			}
		} else {
			for (JComponent component : waitingComponentList) {
				component.setEnabled(isPlay == false);
			}
			for (JComponent component : runningComponentList) {
				component.setEnabled(isPlay == true);
			}
		}
	}

}
