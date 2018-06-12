package com.zdb;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Test;

public class Sha256Test {

	@Test
	public void test1() {
		String password = "zhidaobao123456";
		String salt = "MJPYooEHfJfyyEthjA9X";
		Sha256Hash sh = new Sha256Hash(password, salt);
		System.out.println(sh.toHex());
	}
}
