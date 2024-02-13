package com.gynt.easysettings.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

	public static <T> List<T> toList(Collection<T> values) {
		return new ArrayList<>(values);
	}

}
