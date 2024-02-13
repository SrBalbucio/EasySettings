package com.gynt.easysettings.implementation;

import java.util.ArrayList;

import com.gynt.easysettings.api.Type;

public class Types {

	private ArrayList<Type> types = new ArrayList<>();

	{
		types.add(new SimpleType("STRING", String.class));
		types.add(new SimpleType("PATH", String.class));
		types.add(new SimpleType("FILE", String.class));
		types.add(new SimpleType("BOOLEAN", Boolean.class));
		types.add(new SimpleType("RADIO", Boolean.class));
		types.add(new SimpleType("INTEGER", Integer.class));
		types.add(new SimpleType("DOUBLE", Double.class));
	}

	public void addType(Type type) {
		types.add(type);
	}

	public void removeType(Type type) {
		types.remove(type);
	}

	public Type forType(String type) {
		for (Type t : types) {
			if (t.getType().equalsIgnoreCase(type))
				return t;
		}
		return null;
	}

}
