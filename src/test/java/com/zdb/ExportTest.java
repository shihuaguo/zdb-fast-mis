package com.zdb;

import java.lang.reflect.Field;

import org.junit.Test;

import com.zdb.common.utils.excel.ReflectionUtil;
import com.zdb.modules.customer.entity.Customer;

public class ExportTest {
	
	class A{
		String a1;
		String a2;
		String a3;
		B b;
	}
	
	class B{
		String b1;
		String b2;
		String b3;
	}

	@Test
	public void testGetFieldAndCNames() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Customer c = new Customer();
		c.setCustomerNo("001");
		c.setCustomerName("zhangsan");
		System.out.println(ReflectionUtil.getFieldAndCNames(Customer.class));
		System.out.println(ReflectionUtil.getMethodAndCNames(Customer.class));
		
		A a = new A();
		a.a1 = "a1";
		a.a2 = "a2";
		a.a3 = "a3";
		B b = new B();
		b.b1 = "b1";
		b.b2 = "b2";
		b.b3 = "b3";
		a.b = b;
		Field f = A.class.getDeclaredField("b");
		Object o = f.get(a);
		Field f1 = o.getClass().getDeclaredField("b1");
		System.out.println(f1.get(o));
	}
}
