package com.hacademy.topwar.macro;

public interface MacroActionListener {
	void done(MacroTimeline timeline, int index);
	void add(MacroTimeline timeline);
	void remove(MacroTimeline timeline);
	void clear();
}
