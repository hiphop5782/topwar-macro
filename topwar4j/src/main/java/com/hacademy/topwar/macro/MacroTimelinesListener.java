package com.hacademy.topwar.macro;

public interface MacroTimelinesListener {
	void start(MacroTimelines timelines);
	void cycleStart(MacroTimelines timelines);
	void cycleFinish(MacroTimelines timelines);
	void finish(MacroTimelines timelines);
}
