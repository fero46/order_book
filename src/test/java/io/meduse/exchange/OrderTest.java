package io.meduse.exchange;

import org.junit.jupiter.api.Test;

class OrderTest {

	@Test
	void test() {
		String json = "{ 'id': '24asdfe', 'market': 'btcusdt', 'price': '8.50', 'volume': '1.5', 'type': 'limit', 'direction': 'ask' }";
		Order o = Order.fromMessageBody(json);
		System.out.println(o);
	}

}
