package com.gynt.easysettings.implementation;

import com.gynt.easysettings.api.Setting;
import com.gynt.easysettings.api.Type;

public class SimpleSetting implements Setting {

	private final String displayText;
	private final String tooltipText;
	private final String key;
	private Object value;
	private final Type type;

	public SimpleSetting(String key, String displayText, String tooltipText, Type type) {
		this.key = key;
		this.displayText = displayText;
		this.tooltipText = tooltipText;
		this.type = type;
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

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
	}

}
