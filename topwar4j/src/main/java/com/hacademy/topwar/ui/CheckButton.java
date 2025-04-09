package com.hacademy.topwar.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = false)
public class CheckButton extends JButton {
	private static final long serialVersionUID = 1L;
	public static final Color taskButtonBackground = new Color(240, 240, 240);
	public static final Color taskButtonForeground = new Color(10, 10, 10);
	public static final Font buttonFont = new Font("", Font.BOLD, 14);
	
	private boolean checked;
	private List<JCheckBox> concernedCheckboxes;

	public CheckButton(String text) {
		super(text);
		this.setFont(buttonFont);
		this.setChecked(false);
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
		this.setBackground(!checked ? taskButtonBackground : taskButtonForeground);
		this.setForeground(!checked ? taskButtonForeground : taskButtonBackground);
		
		if(concernedCheckboxes != null) {
			for(JCheckBox checkbox : concernedCheckboxes) {
				checkbox.setSelected(checked);
				for (ActionListener listener : checkbox.getActionListeners()) {
					listener.actionPerformed(new ActionEvent(checkbox, ActionEvent.ACTION_PERFORMED, ""));
				}
			}
		}
	}
	public void setConcernedCheckboxes(List<JCheckBox> concernedCheckboxes) {
		this.concernedCheckboxes = concernedCheckboxes;
		this.refreshChecked();
	}
	public void refreshChecked() {
		this.setChecked(getConcernedCheckboxesChecked());
	}
	public boolean getConcernedCheckboxesChecked() {
		if(concernedCheckboxes == null) return false;
		for(JCheckBox checkbox : concernedCheckboxes) {
			if(!checkbox.isSelected())
				return false;
		}
		return true;
	}
}
