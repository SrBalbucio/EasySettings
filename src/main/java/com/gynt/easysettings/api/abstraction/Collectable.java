package com.gynt.easysettings.api.abstraction;

import java.util.List;

public interface Collectable<T> {

	public void add(T t);

	public void remove(T t);

	public List<T> get();

}
