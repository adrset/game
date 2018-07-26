package xyz.parala.game.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

public class Utils {
	public static ByteBuffer ioResourceToByteBuffer(String resource) throws IOException {
		System.out.println(resource);
		InputStream source = Class.class.getResourceAsStream(resource);
		byte[] _data = IOUtils.toByteArray(source);
		ByteBuffer data = MemoryUtil.memCalloc(_data.length);
		data.put(_data);
		data.flip();
		return data;
	}

}