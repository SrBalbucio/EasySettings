package com.gynt.easysettings.api;

import com.gynt.easysettings.api.abstraction.Settingable;

public interface Setting extends Settingable {

	public Type getType();

	public Object getValue();

	public void setValue(Object value);

	public String getKey();

}
