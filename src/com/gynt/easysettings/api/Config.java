package com.gynt.easysettings.api;

import java.util.List;

import com.gynt.easysettings.api.abstraction.Collectable;
import com.gynt.easysettings.api.abstraction.Keyable;

public interface Config extends Collectable<Directory>, Keyable<Directory> {
	
	public Setting getByHierarchyKey(String key);
	
	public List<String> getKeyHierarchy();

}
