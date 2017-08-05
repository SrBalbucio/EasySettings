package com.gynt.easysettings.implementation;

import com.gynt.easysettings.api.Directory;
import com.gynt.easysettings.api.Section;

public class SimpleDirectory extends Mapper<Section> implements Directory {

	private final String displayText;
	private final String tooltipText;
	private final String key;

	public SimpleDirectory(String key, String displayText, String tooltipText) {
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
