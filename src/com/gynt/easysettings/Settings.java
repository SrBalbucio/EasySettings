package com.gynt.easysettings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class Settings {

	public static class Radio {
		public static Boolean parseRadio(Object val) {
			return Boolean.parseBoolean((String) val);
		}
		public static String toString(Boolean c) {
			return Boolean.toString(c);
		}
	}

	public static Properties PROPERTIES = new Properties();

	public static void save() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PROPERTIES.store(bos, "");
		Path p = new File("settings.properties").toPath();
		save(p, bos.toByteArray());
	}

	private static void save(Path p, byte[] data) throws IOException {
		Files.write(p, data);
	}

	public static void load() throws IOException {
		if(!new File("settings.properties").exists()) {
			String path = Paths.get(new File("").toPath().toString(), "settings.properties").toString();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PROPERTIES.store(bos, "");

			String[] paths = path.split("/+");
			if(paths[0].contains("file")) {
				paths=Arrays.copyOfRange(paths, 1, paths.length);
			}
			path=String.join("/", paths);

			save(new File(path).toPath(), bos.toByteArray());
		}

		PROPERTIES.clear();
		ByteArrayInputStream bis = new ByteArrayInputStream(
				Files.readAllBytes(new File("settings.properties").toPath()));
		PROPERTIES.load(bis);

	}

	public static boolean loaded() {
		return PROPERTIES != null;
	}


	public static PreferenceDir ROOT;
	static {
		ROOT = new PreferenceDir();
		ROOT.name="Preferences";

	}

	public static interface Parentable {
		Parentable getParent();
	}

	public static interface ChangeListener {
		void onChange(Object oldValue, Object newValue);
	}

	public static class PreferenceItem implements Parentable {
		public String name;
		public Class<?> type;
		public PreferenceSub parent;
		private String path;
		public String description;

		public ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();

		public PreferenceItem(PreferenceSub p, String n, Class<?> t, Object val) {
			parent = p;
			name = n;
			type = t;
			path = buildPath();
			setValue(val);
		}

		private void tell(Object oldValue, Object newValue) {
			for(ChangeListener cl : listeners) {
				cl.onChange(oldValue, newValue);
			}
		}

		public void setValue(Object val) {
			Object oval = getValue();
			switch(type.getSimpleName()) {
			case "File":
				PROPERTIES.setProperty(path, val.toString());
				break;
			case "String":
				PROPERTIES.setProperty(path, val.toString());
				break;
			case "Boolean":
				PROPERTIES.setProperty(path, Boolean.toString((Boolean) val));
				break;
			case "Radio":
				PROPERTIES.setProperty(path, Radio.toString((Boolean) val));
				break;
			case "Integer":
				PROPERTIES.setProperty(path, Integer.toString((int) val));
				break;
			case "Double":
				PROPERTIES.setProperty(path, Double.toString((double) val));
				break;
			case "Float":
				PROPERTIES.setProperty(path, Float.toString((float) val));
				break;
			default:
				throw new RuntimeException();
			}
			if(oval!=null) {
				if(!oval.equals(val)) tell(oval, val);
			} else {
				tell(oval, val);
			}
		}

		public Object getValue() {
			switch(type.getSimpleName()) {
			case "File":
				return PROPERTIES.getProperty(path)!=null?new File(PROPERTIES.getProperty(path)):new File("");
			case "String":
				return PROPERTIES.getProperty(path);
			case "Boolean":
				return Boolean.parseBoolean(PROPERTIES.getProperty(path));
			case "Radio":
				return Radio.parseRadio(PROPERTIES.getProperty(path));
			case "Integer":
				return Integer.parseInt(PROPERTIES.getProperty(path));
			case "Double":
				return Double.parseDouble(PROPERTIES.getProperty(path)==null?"0.0":PROPERTIES.getProperty(path));
			case "Float":
				return Float.parseFloat(PROPERTIES.getProperty(path)==null?"0.0":PROPERTIES.getProperty(path));
			default:
				throw new RuntimeException();
			}
		}

		public String buildPath() {
			String path = name;
			Parentable parent = getParent();
			while(parent!=null) {
				path=parent.toString()+"."+path;
				parent = parent.getParent();
			}
			return path;
		}

		@Override
		public Parentable getParent() {
			return parent;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static class PreferenceSub implements Parentable {
		public String name;
		public PreferenceDir parent;
		public ArrayList<PreferenceItem> items = new ArrayList<>();
		public String description;

		public PreferenceItem lookUp(String name) {
			for(PreferenceItem child : items) {
				if(child.name.equals(name))  return child;
			}
			return null;
		}

		public PreferenceItem registerItem(String name, String description, Class<?> type, Object defaultvalue) {
			PreferenceItem i = lookUp(name);
			if(i!=null) return i;

			PreferenceItem result = new PreferenceItem(this, name, type, defaultvalue);
			result.description=description;
			items.add(result);
			return result;
		}

		@Override
		public Parentable getParent() {
			return parent;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static class PreferenceDir implements Parentable {

		public String name;
		public PreferenceDir parent;
		public String description;

		public ArrayList<PreferenceDir> dirs = new ArrayList<>();
		public ArrayList<PreferenceSub> subs = new ArrayList<>();

		public PreferenceDir lookUpDir(String name) {
			for(PreferenceDir child : this.dirs) {
				if(child.name==name) {
					return child;
				}
			}
			return null;
		}

		public PreferenceSub lookUpSub(String name) {
			for(PreferenceSub child : this.subs) {
				if(child.name==name) {
					return child;
				}
			}
			return null;
		}

		public PreferenceDir registerDir(String string) {
			PreferenceDir i = lookUpDir(string);
			if(i!=null) return i;

			PreferenceDir result = new PreferenceDir();
			result.name=string;
			result.parent=this;
			dirs.add(result);
			return result;
		}

		public PreferenceSub registerSub(String string, String description) {
			PreferenceSub i = lookUpSub(string);
			if(i!=null) return i;

			PreferenceSub result = new PreferenceSub();
			result.name=string;
			result.parent=this;
			result.description=description;
			subs.add(result);
			return result;
		}

		@Override
		public Parentable getParent() {
			return parent;
		}

		@Override
		public String toString() {
			return name;
		}
	}


	public Settings() {

	}

}
