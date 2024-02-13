package com.gynt.easysettings.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Util {

	public static <T> List<T> toList(Collection<T> values) {
		ArrayList<T> list = new ArrayList<>();
		for (T v : values)
			list.add(v);
		return list;
	}

}
