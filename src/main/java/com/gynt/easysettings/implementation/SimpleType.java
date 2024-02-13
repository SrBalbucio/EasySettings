package com.gynt.easysettings.implementation;

import com.gynt.easysettings.api.Type;

public class SimpleType implements Type {

	private String type;
	private Class<?> datatype;

	public SimpleType(String type, Class<?> datatype) {
		this.type = type;
		this.datatype = datatype;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Class<?> getDataType() {
		return datatype;
	}

}
