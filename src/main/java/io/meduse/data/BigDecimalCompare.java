package io.meduse.data;

import java.math.BigDecimal;

import com.github.rohansuri.art.BinaryComparable;

public class BigDecimalCompare implements BinaryComparable<BigDecimal> {

	@Override
	public byte[] get(BigDecimal key) {
		return key.unscaledValue().toByteArray();
	}

}
