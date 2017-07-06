package com.dianping.cat.message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.dianping.cat.Cat;

import java.io.IOException;

@RunWith(JUnit4.class)
public class TransactionTest {
	@Test
	public void testNormal() throws IOException {
		Transaction t = Cat.getProducer().newTransaction("URL11", "MyPage");

		try {
			// do your business here
			t.addData("k1", "v1");
			t.addData("k2", "v2");
			t.addData("k3", "v3");
			Thread.sleep(30);

			t.setStatus(Message.SUCCESS);
		} catch (Exception e) {
			t.setStatus(e);
		} finally {
			t.complete();
		}

		System.in.read();
	}
}
