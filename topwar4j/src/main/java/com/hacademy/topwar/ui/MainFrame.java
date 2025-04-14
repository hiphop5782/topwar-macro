package com.hacademy.topwar.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

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
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import com.hacademy.topwar.constant.Delay;
import com.hacademy.topwar.macro.MacroCreator;
import com.hacademy.topwar.macro.MacroStatus;
import com.hacademy.topwar.macro.MacroTimelines;
import com.hacademy.topwar.macro.MacroTimelinesGroup;
import com.hacademy.topwar.macro.MacroTimelinesListener;
import com.hacademy.topwar.ui.components.CheckButton;
import com.hacademy.topwar.ui.components.NumberField;
import com.hacademy.topwar.ui.components.StatusCheckBox;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

@Getter
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private MacroStatus status = MacroStatus.getInstance();

	private int macroExecuteCount = 0;

	private MacroTimelinesListener macroTimelinesListener = new MacroTimelinesListener() {
		@Override
		public void start(MacroTimelines timelines) {
			macroExecuteCount = 0;
			setPlayingState(true);
		}

		@Override
		public void finish(MacroTimelines timelines) {
			setPlayingState(false);
		}

		@Override
		public void cycleStart(MacroTimelines timelines) {
			macroExecuteCount++;
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

	private List<JComponent> waitingComponentList = new ArrayList<>();
	private List<JComponent> runningComponentList = new ArrayList<>();
	
	private JScrollPane jsp = new JScrollPane();
	private JTextArea jtx = new JTextArea();
	
	private List<JComponent> maximizeComponents = new ArrayList<>();
	private List<JComponent> minimizeComponents = new ArrayList<>();
	private boolean mini = false;
	
	private JComboBox<String> screenSelectBox = new JComboBox<>();
	private NumberField periodInput = new NumberField();
	private String noticeText;
	
	private List<StatusCheckBox> dailyTaskCheckboxes = new ArrayList<>();
	private List<StatusCheckBox> weeklyTaskCheckboxes = new ArrayList<>();
	private List<StatusCheckBox> etcTaskCheckboxes = new ArrayList<>();
	private List<StatusCheckBox> facilityTaskCheckboxes = new ArrayList<>();
	
	public MainFrame() throws Exception {
		WindowStatus ws = WindowStatus.load();
		this.setAlwaysOnTop(false);
		if(ws == null) 
			this.setLocationByPlatform(true);
		else 
			this.setLocation(ws.getX(), ws.getY());
		this.setResizable(false);
		this.setTitle("TW-Macro (설정된 화면 : " + status.getScreenList().size() + ")");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitProgram();
			}
		});
		this.init();
		this.pack();
		if(status != null && ws != null) {
			this.setMinimode(ws.isMini());
		}
		this.refreshScreenSelectBox();
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
				setTitle("TW-Macro (설정된 화면 : " + status.getScreenList().size() + ")");
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
		JPanel statusPanel = new JPanel(new MigLayout("wrap 2", "[grow,fill]15[grow,fill]"));

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

//		JCheckBox useVit = new JCheckBox("물약 사용", status.isPotion());
//		useVit.addActionListener(e -> {
//			status.setPotion(useVit.isSelected());
//		});
		StatusCheckBox useVit = new StatusCheckBox("물약 사용", "potion");

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
		
		NumberField durationField = new NumberField();
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
		
		//텍스트 자동발송 설정
		JPanel noticePanel = new JPanel(new MigLayout("wrap 5", "[grow]30[][]30[]10[]", ""));
		noticePanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "반복 텍스트 입력"));
		waitingComponentList.add(noticePanel);
		noticePanel.add(screenSelectBox, "grow");
		
		periodInput.setText("60");
		waitingComponentList.add(periodInput);
		noticePanel.add(periodInput, "grow");
		
		JLabel lb = new JLabel("분마다 반복");
		noticePanel.add(lb);
		
		JButton noticeTextButton = new JButton("텍스트 설정");
		waitingComponentList.add(noticeTextButton);
		noticePanel.add(noticeTextButton);
		
		JButton noticePlayButton = new JButton("자동입력 시작");
		waitingComponentList.add(noticePlayButton);
		noticePlayButton.setEnabled(false);
		
		noticeTextButton.addActionListener(e->{
			noticeText = NoticeInputDialog.showDialog(this);
			noticePlayButton.setEnabled(noticeText != null);
		});
		noticePlayButton.addActionListener(e->{
			try {
				playNoticeMacro();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		noticePanel.add(noticePlayButton);
		
		contentPanel.add(noticePanel);
		
		//반복 작업
		JPanel taskPanel = new JPanel(new MigLayout("wrap 1", "[grow]", ""));
		taskPanel.setBorder(BorderFactory.createTitledBorder(lineBorder2, "반복 작업"));

		JPanel dailyTaskPanel = new JPanel(new MigLayout("wrap 5", "[][][][][]", ""));
		dailyTaskPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "일일 작업"));

		dailyTaskCheckboxes.add(new StatusCheckBox("VIP보상", "dailyVipReward", dailyTaskCheckButton));
		dailyTaskCheckboxes.add(new StatusCheckBox("장바구니", "dailyBasketReward", dailyTaskCheckButton));
		dailyTaskCheckboxes.add(new StatusCheckBox("특별패키지", "dailySpecialReward", dailyTaskCheckButton));
		dailyTaskCheckboxes.add(new StatusCheckBox("사판훈련", "dailySandTraning", dailyTaskCheckButton));
		dailyTaskCheckboxes.add(new StatusCheckBox("일반&스킬모집", "dailyNormalIncrutAndSkill", dailyTaskCheckButton));
		dailyTaskCheckboxes.add(new StatusCheckBox("고급모집(2회)", "dailyAdvancedIncruit", dailyTaskCheckButton));
		dailyTaskCheckboxes.add(new StatusCheckBox("일일업무", "dailyQuestReward", dailyTaskCheckButton));
		dailyTaskCheckboxes.add(new StatusCheckBox("크로스패배(10회)", "dailyCrossBattle", dailyTaskCheckButton));
		dailyTaskCheckboxes.add(new StatusCheckBox("무료다이아(20회)", "dailyGemReward", dailyTaskCheckButton));
		for (StatusCheckBox checkbox : dailyTaskCheckboxes) {
			dailyTaskPanel.add(checkbox);
			waitingComponentList.add(checkbox);
		}
		taskPanel.add(dailyTaskPanel, "grow");

		JPanel rowPanel = new JPanel(new MigLayout("inset 0", "[fill,grow][fill,grow]", ""));

		// 주간퀘스트
		JPanel weeklyTaskPanel = new JPanel(new MigLayout("", "[]", ""));
		weeklyTaskPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "주간 작업"));

		weeklyTaskCheckboxes.add(new StatusCheckBox("무료장식토큰", "weeklyDecorFreeToken", weeklyTaskCheckButton));
		
		for (JCheckBox checkbox : weeklyTaskCheckboxes) {
			weeklyTaskPanel.add(checkbox);
			waitingComponentList.add(checkbox);
		}

		rowPanel.add(weeklyTaskPanel);
		
		// 기타 작업
		JPanel etcTaskPanel = new JPanel(new MigLayout("wrap 3", "[]10[]10[]", ""));
		etcTaskPanel.setBorder(BorderFactory.createTitledBorder(lineBorder1, "기타 작업"));
		
		JPanel allianceDonationPanel = new JPanel(new MigLayout("inset 0", "", ""));
		StatusCheckBox allianceDonationCheckbox = new StatusCheckBox("길드기부(10회)", "allianceDonation", etcTaskCheckButton); 
		allianceDonationPanel.add(allianceDonationCheckbox);
		etcTaskPanel.add(allianceDonationPanel);
		
		JPanel materialPanel = new JPanel(new MigLayout("inset 0", "[grow][]", ""));
		JComboBox<String> materialTypebox = new JComboBox<>(new String[] {"강철","나사","트랜지스터", "고무", "텅스텐", "배터리", "유리"});
		StatusCheckBox materialCheckbox = new StatusCheckBox("재료생산", "productMaterial", etcTaskCheckButton, materialTypebox);
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
		JComboBox<Integer> oilTaskLevel = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5 });
		StatusCheckBox oilTaskCheckbox = new StatusCheckBox("석유시설", "oilFacility", facilityTaskCheckButton, oilTaskLevel);
		oilTaskLevel.setSelectedItem(status.getOilFacilityLevel());
		oilTaskLevel.addActionListener(e -> {
			status.setOilFacilityLevel((int) oilTaskLevel.getSelectedItem());
		});

		oilTaskPanel.add(oilTaskCheckbox);
		oilTaskPanel.add(oilTaskLevel);

		facilityTaskPanel.add(oilTaskPanel);

		JPanel foodTaskPanel = new JPanel(new MigLayout("inset 0", "[grow][]", ""));
		JComboBox<Integer> foodTaskLevel = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5 });
		StatusCheckBox foodTaskCheckbox = new StatusCheckBox("식량시설", "foodFacility", facilityTaskCheckButton, foodTaskLevel);
		foodTaskLevel.setSelectedItem(status.getFoodFacilityLevel());
		foodTaskLevel.addActionListener(e -> {
			status.setFoodFacilityLevel((int) foodTaskLevel.getSelectedItem());
		});

		foodTaskPanel.add(foodTaskCheckbox);
		foodTaskPanel.add(foodTaskLevel);

		facilityTaskPanel.add(foodTaskPanel);

		JPanel odinTaskPanel = new JPanel(new MigLayout("inset 0", "[grow][]", ""));
		JComboBox<Integer> odinTaskLevel = new JComboBox<>(new Integer[] { 1, 2, 3 });
		StatusCheckBox odinTaskCheckbox = new StatusCheckBox("오딘시설", "odinFacility", facilityTaskCheckButton, odinTaskLevel);
		odinTaskLevel.setSelectedItem(status.getOdinFacilityLevel());
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

		dailyTaskCheckButton.setConcernedCheckboxes(dailyTaskCheckboxes);
		weeklyTaskCheckButton.setConcernedCheckboxes(weeklyTaskCheckboxes);
		etcTaskCheckButton.setConcernedCheckboxes(etcTaskCheckboxes);
		facilityTaskCheckButton.setConcernedCheckboxes(facilityTaskCheckboxes);
		taskButtonPanel.add(dailyTaskCheckButton);
		taskButtonPanel.add(weeklyTaskCheckButton);
		taskButtonPanel.add(etcTaskCheckButton);
		taskButtonPanel.add(facilityTaskCheckButton);
		
		waitingComponentList.add(dailyTaskCheckButton);
		waitingComponentList.add(weeklyTaskCheckButton);
		waitingComponentList.add(etcTaskCheckButton);
		waitingComponentList.add(facilityTaskCheckButton);
		dailyTaskCheckButton.refreshChecked();
		weeklyTaskCheckButton.refreshChecked();
		etcTaskCheckButton.refreshChecked();
		facilityTaskCheckButton.refreshChecked();

		taskRunButton.setBackground(new Color(46, 204, 113));
		taskRunButton.setForeground(Color.white);
		taskRunButton.setFont(buttonFont);
		ActionListener runTask = e->{
			long count = Stream.of(dailyTaskCheckboxes, weeklyTaskCheckboxes, facilityTaskCheckboxes, etcTaskCheckboxes)
					.flatMap(Collection::stream)
					.filter(checkbox->checkbox.isSelected())
					.count();

			if (count == 0) {
				JOptionPane.showMessageDialog(this, "1개 이상의 작업을 선택하세요");
				return;
			}

			try {
				playTaskMacro();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		};
		taskRunButton.setBackground(new Color(46, 204, 113));
		taskRunButton.setForeground(Color.white);
		taskRunButton.setFont(buttonFont);
		taskRunButton.addActionListener(runTask);

		taskButtonPanel.add(taskRunButton);
		taskPanel.add(taskButtonPanel, "growx");
		
		contentPanel.add(taskPanel);

		waitingComponentList.add(taskRunButton);
		
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
		
		JButton areaRemoveButton2 = new JButton("영역제거");
		areaRemoveButton2.setBackground(new Color(243, 156, 18));
		areaRemoveButton2.setForeground(Color.white);
		areaRemoveButton2.setFont(buttonFont);
		areaRemoveButton2.addActionListener(e -> removeScreenRect());
		waitingComponentList.add(areaRemoveButton2);
		
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
		waitingComponentList.add(warhammerCustomButton2);
		
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
		waitingComponentList.add(terror4kLoopButton2);
		
		JButton taskRunButton2 = new JButton("일일업무");
		taskRunButton2.setBackground(taskRunButton.getBackground());
		taskRunButton2.setForeground(taskRunButton.getForeground());
		taskRunButton2.setFont(taskRunButton.getFont());
		taskRunButton2.addActionListener(runTask);
		minimizePanel.add(taskRunButton2);
		waitingComponentList.add(taskRunButton2);

		
		JButton facilityButton = new JButton("시설");
		facilityButton.setBackground(new Color(42, 52, 54));
		facilityButton.setForeground(Color.white);
		facilityButton.setFont(buttonFont);
		facilityButton.addActionListener(e->{
			try {
				playFacilityMacro();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		waitingComponentList.add(facilityButton);

		JButton macroStopButton2 = new JButton("실행중지");
		macroStopButton2.setBackground(macroStopButton.getBackground());
		macroStopButton2.setForeground(macroStopButton.getForeground());
		macroStopButton2.setFont(macroStopButton.getFont());
		macroStopButton2.addActionListener(e -> stopMacro());
		runningComponentList.add(macroStopButton2);
		
		minimizePanel.add(areaButton2);
		minimizePanel.add(areaRemoveButton2);
		minimizePanel.add(darkforceInputButton2);
		minimizePanel.add(warhammerCustomButton2);
		minimizePanel.add(terror4kLoopButton2);
		minimizePanel.add(taskRunButton2);
		minimizePanel.add(facilityButton);
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
	
	private void refreshScreenSelectBox() {
		screenSelectBox.removeAllItems();
		if(status.getScreenList() == null) return;
		for(int i=0; i < status.getScreenList().size(); i++) {
			Rectangle rect = status.getScreenList().get(i);
			screenSelectBox.addItem("화면 "+(i+1)+" - ("+rect.x+","+rect.y+","+rect.width+","+rect.height+")");
		}
	}

	private void addScreenRect() {
		if (timelinesGroup.isPlaying())
			return;
		Rectangle screenRect = ScreenRectDialog.showDialog(MainFrame.this);
		status.getScreenList().add(screenRect);
		setTitle("TW-Macro (설정된 화면 : " + status.getScreenList().size() + ")");
		refreshScreenSelectBox();
		setPlayingState(false);
	}

	private void removeScreenRect() {
		if (timelinesGroup.isPlaying())
			return;
		if (status.getScreenList().isEmpty())
			return;
		status.getScreenList().remove(status.getScreenList().size() - 1);
		setTitle("TW-Macro (설정된 화면 : " + status.getScreenList().size() + ")");
		refreshScreenSelectBox();
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
	
	private void playFacilityMacro() throws Exception {
		if (status.getScreenList().isEmpty())
			return;
		if (timelinesGroup.isPlaying())
			return;
		MacroCreator.facility(timelinesGroup, status);
		timelinesGroup.playOnce();
		setPlayingState(true);
	}

	private void stopMacro() {
		if (timelinesGroup.isPlaying()) {
			timelinesGroup.stop();
		}
	}
	
	private void playNoticeMacro() throws Exception {
		if (status.getScreenList().isEmpty())
			return;
		if (timelinesGroup.isPlaying())
			return;
		if (noticeText == null) 
			return;
		
		String screenText = (String)screenSelectBox.getSelectedItem();
		screenText = screenText.substring(0, screenText.indexOf("-"));
		int screenNumber = Integer.parseInt(screenText.replace("화면", "").replace(" ", ""));
		String periodText = periodInput.getText();
		if(periodText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "텍스트 실행 간격을 설정하세요");
			return;
		}
		int period = Integer.parseInt(periodText);
		MacroCreator.notice(timelinesGroup, status, screenNumber, period, noticeText);
		timelinesGroup.play();
		setPlayingState(true);
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
