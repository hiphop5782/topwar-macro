package com.hacademy.topwar.ui;

import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import com.hacademy.topwar.macro.MacroStatus;

public class StatusCheckBox extends JCheckBox {
	private static final long serialVersionUID = 1L;
	private final String statusProperty;
	private CheckButton parent;
	private JComponent sibling;
	
	private boolean loading = false;
	
	private ActionListener dispatchHandler = e->{
		if(loading) return;
		
		saveStatus();
		
		if(parent != null) {
			parent.refreshChecked();
		}
		
		if(sibling != null) {
			sibling.setEnabled(isSelected());
		}
	};
	
	public StatusCheckBox(String name, String statusProperty) {
		super(name);
		this.statusProperty = statusProperty;
		this.addActionListener(dispatchHandler);
		this.loadStatus();
	}
	public StatusCheckBox(String name, String statusProperty, CheckButton parent) {
		this(name, statusProperty);
		this.parent = parent;
	}
	public StatusCheckBox(String name, String statusProperty, CheckButton parent, JComponent sibling) {
		this(name, statusProperty, parent);
		this.sibling = sibling;
	}
	
	private void loadStatus() {
		try {
			loading = true;
			Field field = MacroStatus.class.getDeclaredField(statusProperty);
			field.setAccessible(true);
			boolean b = field.getBoolean(MacroStatus.getInstance());
			super.setSelected(b);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			loading = false;
		}
	}
	private void saveStatus() {
		try {
			MacroStatus instance = MacroStatus.getInstance();
			Field field = MacroStatus.class.getDeclaredField(statusProperty);
			field.setAccessible(true);
			field.set(instance, isSelected());
			instance.save();
			System.out.println("** 설정 내보내기 완료 **");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setSelected(boolean checked) {
		super.setSelected(checked);
		
		if(sibling != null) {
			sibling.setEnabled(checked);
		}
	}
	public void setSelectedAndSave(boolean checked) {
		this.setSelected(checked);
		
		try {
			MacroStatus instance = MacroStatus.getInstance();
			Field field = MacroStatus.class.getDeclaredField(statusProperty);
			field.setAccessible(true);
			field.set(instance, checked);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
