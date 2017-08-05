package com.gynt.easysettings.implementation;

import java.util.ArrayList;
import java.util.List;

import com.gynt.easysettings.api.Config;
import com.gynt.easysettings.api.Directory;
import com.gynt.easysettings.api.Section;
import com.gynt.easysettings.api.Setting;

public class SimpleConfig extends Mapper<Directory> implements Config {

	@Override
	public Setting getByHierarchyKey(String key) {
		String[] keys = key.split("\\.");
		Directory d = get(keys[0]);
		if(d!=null) {
			Section s = d.get(keys[1]);
			if(s!=null) {
				Setting se = s.get(keys[2]);
				return se;
			}
		}
		return null;
	}

	@Override
	public List<String> getKeyHierarchy() {
		ArrayList<String> result = new ArrayList<>();
		for(Directory d : get()) {
			String r = d.getKey();
			for(Section s : d.get()) {
				String re = r+"."+s.getKey();
				for(Setting ss : s.get()) {
					String res = re+"."+ss.getKey();
					result.add(res);
				}
			}
		}
		return result;
	}

}
