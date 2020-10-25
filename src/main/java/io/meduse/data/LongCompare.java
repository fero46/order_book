package io.meduse.data;

import java.nio.ByteBuffer;

import com.github.rohansuri.art.BinaryComparable;

public class LongCompare implements BinaryComparable<Long> {
	private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

	@Override
	public byte[] get(Long key) {
		buffer.putLong(key);
		return buffer.array();
	}

}
