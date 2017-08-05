package com.gynt.easysettings.implementation;

import com.gynt.easysettings.api.Section;
import com.gynt.easysettings.api.Setting;

public class SimpleSection extends Mapper<Setting> implements Section {

	private final String displayText;
	private final String tooltipText;
	private final String key;

	public SimpleSection(String key, String displayText, String tooltipText) {
		this.key = key;
		this.displayText = displayText;
		this.tooltipText = tooltipText;
	}

	@Override
	public String getDisplayText() {
		return displayText;
	}

	@Override
	public String getTooltipText() {
		return tooltipText;
	}

	@Override
	public String getKey() {
		return key;
	}

}
