package com.gynt.easysettings.api;

public interface IO {

	public byte[] serialize(Config config);

	public Config deserialize(byte[] data);

}
