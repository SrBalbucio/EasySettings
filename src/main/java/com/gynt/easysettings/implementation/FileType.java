package com.gynt.easysettings.implementation;

public class FileType extends SimpleType {

	private String extension;

	public FileType(String type, Class<?> datatype, String extension) {
		super(type, datatype);
		this.extension = extension;
	}

	public String getExtension() {
		return this.extension;
	}

}
