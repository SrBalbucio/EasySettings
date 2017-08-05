package com.gynt.easysettings.implementation;

import java.util.LinkedHashMap;
import java.util.List;

import com.gynt.easysettings.api.abstraction.Collectable;
import com.gynt.easysettings.api.abstraction.Keyable;
import com.gynt.easysettings.api.abstraction.Settingable;

public class Mapper<T extends Settingable> implements Collectable<T>, Keyable<T> {

	private LinkedHashMap<String, T> map = new LinkedHashMap<>();

	@Override
	public T get(String key) {
		return map.get(key);
	}

	@Override
	public void add(T t) {
		map.put(t.getKey(), t);
	}

	@Override
	public void remove(T t) {
		map.remove(t.getKey());
	}

	@Override
	public List<T> get() {
		return Util.toList(map.values());
	}

}
