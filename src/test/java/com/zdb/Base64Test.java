package com.zdb;

import java.util.Base64;
import java.util.Base64.Encoder;

import org.junit.Test;

public class Base64Test {

	@Test
	public void test1() {
		Encoder encoder = Base64.getEncoder();
		byte[] bb = "Long860315".getBytes();
		byte[] bb1 = encoder.encode(bb);
		System.out.println(new String(bb1));
	}
}
