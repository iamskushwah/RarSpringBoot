package com.wellsfargo.rarconsumer.util;

import java.util.Random;

public class FieldIdGenerator {
	public static Long getFieldId() {
		long generatedLong = new Random().nextInt(Integer.MAX_VALUE);
		return generatedLong;
	}
}
