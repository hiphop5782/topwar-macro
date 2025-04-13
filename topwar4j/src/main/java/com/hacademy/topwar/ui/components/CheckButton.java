package com.hacademy.topwar.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import com.hacademy.topwar.macro.MacroStatus;

public class CheckButton extends JButton {
	private static final long serialVersionUID = 1L;
	public static final Color taskButtonBackground = new Color(240, 240, 240);
	public static final Color taskButtonForeground = new Color(10, 10, 10);
	public static final Font buttonFont = new Font("", Font.BOLD, 14);
	
	private boolean checked;
	private List<StatusCheckBox> concernedCheckboxes;
	
	private ActionListener clickHandler = e->{
		this.setChecked(!checked);
		if(concernedCheckboxes == null) return;
		this.dispatchChecked();
	};

	public CheckButton(String text) {
		super(text);
		this.setFont(buttonFont);
		this.setChecked(false);
		this.addActionListener(clickHandler);
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
		this.setBackground(!checked ? taskButtonBackground : taskButtonForeground);
		this.setForeground(!checked ? taskButtonForeground : taskButtonBackground);
	}
	public void setConcernedCheckboxes(List<StatusCheckBox> concernedCheckboxes) {
		this.concernedCheckboxes = concernedCheckboxes;
	}
	public void refreshChecked() {
		this.setChecked(getConcernedCheckboxesChecked());
	}
	public void dispatchChecked() {
		if(concernedCheckboxes != null) {
			for(StatusCheckBox checkbox : concernedCheckboxes) {
				checkbox.setSelectedAndSave(checked);
			}
			MacroStatus.getInstance().save();
		}
	}
	public boolean getConcernedCheckboxesChecked() {
		if(concernedCheckboxes == null) return false;
		for(StatusCheckBox checkbox : concernedCheckboxes) {
			if(!checkbox.isSelected())
				return false;
		}
		return true;
	}
	public boolean isChecked() {
		return checked;
	}
}
