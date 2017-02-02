package com.gynt.easysettings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

public class Settings {

	private Dir ROOT;

	private File save;
	private File load;
	public final Properties PROPERTIES = new Properties();

	public static class Properties extends java.util.Properties {

		/**
		 *
		 */
		private static final long serialVersionUID = 3339584917734731721L;
		@Override
		public synchronized Enumeration<Object> keys() {
			ArrayList<Object> result = Collections.list(super.keys());
			Collections.sort(result, new Comparator<Object>() {

				@Override
				public int compare(Object o1, Object o2) {
					return o1.toString().compareTo(o2.toString());
				}
			});
			return Collections.enumeration(result);
		}
	}

	public Dir getRoot() {
		return ROOT;
	}

	public Settings(File loadFile) {
		load = loadFile;
		ROOT = new Dir();
		ROOT.name = "Preferences";
	}

	public Settings(File loadFile, File saveFile) {
		this(loadFile);
		save = saveFile;
	}

	public void setLoadFile(File f) {
		load = f;
	}

	public void setSaveFile(File f) {
		save = f;
	}

	public void saveDefault() throws IOException {
		if (save == null)
			throw new RuntimeException("Save file not specified");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PROPERTIES.store(bos, "");
		save(save.toPath(), bos.toByteArray());
	}

	public void save() throws IOException {
		save(save.toPath(), getBytes());
	}

	private void save(Path p, byte[] data) throws IOException {
		Files.write(p, data);
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PROPERTIES.store(bos, "");
		return bos.toByteArray();
	}

	public void fromBytes(byte[] bytes) throws IOException {
		PROPERTIES.load(new ByteArrayInputStream(bytes));
	}

	public void load() throws IOException {
		fromBytes(Files.readAllBytes(load.toPath()));
	}

	public static enum Type {
		FOLDER(File.class), FILE(File.class), RADIO(Boolean.class), BOOLEAN(Boolean.class), STRING(
				String.class), INTEGER(
						int.class), DOUBLE(double.class), FLOAT(float.class), SHORT(short.class), LONG(long.class);

		public Class<?> type;

		private Type(Class<?> t) {
			type = t;
		}
	}

	public static interface Parentable {
		Parentable getParent();
	}

	public static interface ChangeListener {
		void onChange(Item source, Object oldValue, Object newValue);
	}

	public class Item implements Parentable {
		public String name;
		public Type type;
		public Sub parent;
		private String path;
		public String description;

		public ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();

		public Item(Sub p, String n, Type t, Object val) {
			parent = p;
			name = n;
			type = t;
			path = buildPath();
			setValue(val);
		}

		private void tell(Object oldValue, Object newValue) {
			for (ChangeListener cl : listeners) {
				cl.onChange(this, oldValue, newValue);
			}
		}

		public void setValue(Object val) {
			Object oval = getValue();
			switch (type) {
			case FOLDER:
				PROPERTIES.setProperty(path, val.toString());
				break;
			case FILE:
				PROPERTIES.setProperty(path, val.toString());
				break;
			case STRING:
				PROPERTIES.setProperty(path, val.toString());
				break;
			case BOOLEAN:
				PROPERTIES.setProperty(path, Boolean.toString((Boolean) val));
				break;
			case RADIO:
				PROPERTIES.setProperty(path, Boolean.toString((Boolean) val));
				break;
			case INTEGER:
				PROPERTIES.setProperty(path, Integer.toString((int) val));
				break;
			case DOUBLE:
				PROPERTIES.setProperty(path, Double.toString((double) val));
				break;
			case FLOAT:
				PROPERTIES.setProperty(path, Float.toString((float) val));
				break;
			case LONG:
				PROPERTIES.setProperty(path, Long.toString((long) val));
				break;
			case SHORT:
				PROPERTIES.setProperty(path, Short.toString((short) val));
				break;
			}
			if (oval != null) {
				if (!oval.equals(val))
					tell(oval, val);
			} else {
				tell(oval, val);
			}
		}

		public Object getValue() {
			String s = PROPERTIES.getProperty(path);
			if(s==null) {
				return null;
			}
			switch (type) {
			case FOLDER:
				return PROPERTIES.getProperty(path) != null ? new File(PROPERTIES.getProperty(path)) : new File("");
			case FILE:
				return PROPERTIES.getProperty(path) != null ? new File(PROPERTIES.getProperty(path)) : new File("");
			case STRING:
				return PROPERTIES.getProperty(path);
			case BOOLEAN:
				return Boolean.parseBoolean(PROPERTIES.getProperty(path));
			case RADIO:
				return Boolean.parseBoolean(PROPERTIES.getProperty(path));
			case INTEGER:
				return Integer.parseInt(PROPERTIES.getProperty(path));
			case DOUBLE:
				return Double.parseDouble(PROPERTIES.getProperty(path));
			case FLOAT:
				return Float.parseFloat(PROPERTIES.getProperty(path));
			case LONG:
				return Long.parseLong(PROPERTIES.getProperty(path));
			case SHORT:
				return Short.parseShort(PROPERTIES.getProperty(path));
			}
			throw new RuntimeException();
		}

		public String buildPath() {
			String path = name;
			Parentable parent = getParent();
			while (parent != null) {
				path = parent.toString() + "." + path;
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

	public class Sub implements Parentable {
		public String name;
		public Dir parent;
		public ArrayList<Item> items = new ArrayList<>();
		public String description;

		public Item lookUp(String name) {
			for (Item child : items) {
				if (child.name.equals(name))
					return child;
			}
			return null;
		}

		public Item registerItem(String name, String description, Type type, Object defaultvalue, ChangeListener...changeListeners) {
			Item i = lookUp(name);
			if (i != null)
				return i;

			Item result = new Item(this, name, type, defaultvalue);
			result.description = description;
			items.add(result);
			for(ChangeListener c : changeListeners) {
				result.listeners.add(c);
			}
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

	public class Dir implements Parentable {

		public String name;
		public Dir parent;
		public String description;

		public ArrayList<Dir> dirs = new ArrayList<>();
		public ArrayList<Sub> subs = new ArrayList<>();

		public Dir lookUpDir(String name) {
			for (Dir child : this.dirs) {
				if (child.name == name) {
					return child;
				}
			}
			return null;
		}

		public Sub lookUpSub(String name) {
			for (Sub child : this.subs) {
				if (child.name == name) {
					return child;
				}
			}
			return null;
		}

		public Dir registerDir(String string) {
			Dir i = lookUpDir(string);
			if (i != null)
				return i;

			Dir result = new Dir();
			result.name = string;
			result.parent = this;
			dirs.add(result);
			return result;
		}

		public Sub registerSub(String string, String description) {
			Sub i = lookUpSub(string);
			if (i != null)
				return i;

			Sub result = new Sub();
			result.name = string;
			result.parent = this;
			result.description = description;
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

}
