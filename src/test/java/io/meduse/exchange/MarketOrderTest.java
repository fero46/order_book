package io.meduse.exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.NavigableSet;

import org.junit.jupiter.api.Test;

import io.meduse.data.Bucket;
import io.meduse.messages.OrderMessage;
import io.meduse.processors.MarketOrder;

public class MarketOrderTest {

	@Test
	void testAskOrder() {
		System.out.println("Start Market Sale");
		OrderBook market = new OrderBook();
		Long volume = 10l;
		Long price = 895l;
		Order bid = new Order("1", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
		market.add(bid);
		bid = new Order("2", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
		market.add(bid);
		price = 901l;
		bid = new Order("3", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
		market.add(bid);
		Order marketOrder = new Order("4", "btcusdt", 0l, 25l, Order.MARKET_ORDER, Order.ASK);
		List<OrderMessage> messages = new MarketOrder(market, marketOrder).process();

		for (int i = 0; i < 3; i++) {
			assertEquals("io.meduse.messages.OrderMatch", messages.get(i).getClass().getName());
		}
		assertEquals(3, messages.size());

		for (OrderMessage message : messages) {
			System.out.println(message.tickerMessage());
		}

		NavigableSet<Long> bids = market.getBids();
		Map<Long, Bucket> buckets = market.getBidBucket();
		assertEquals(1, bids.size());
		assertEquals(5l, buckets.get(895l).getVolume());
		assertEquals(market.getLastPrice(), 895l);
	}

	@Test
	void testBidOrder() {
		System.out.println("Start Market Buy");
		OrderBook market = new OrderBook();
		Long volume = 10l;
		Long price = 895l;
		Order ask = new Order("1", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
		market.add(ask);
		ask = new Order("2", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
		market.add(ask);
		price = 901l;
		ask = new Order("3", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
		market.add(ask);
		Order marketOrder = new Order("4", "btcusdt", 0l, 25l, Order.MARKET_ORDER, Order.BID);
		List<OrderMessage> messages = new MarketOrder(market, marketOrder).process();

		assertEquals(3, messages.size());
		for (int i = 0; i < 3; i++) {
			assertEquals("io.meduse.messages.OrderMatch", messages.get(i).getClass().getName());
		}
		for (OrderMessage message : messages) {
			System.out.println(message.tickerMessage());
		}

		NavigableSet<Long> asks = market.getAsks();
		Map<Long, Bucket> buckets = market.getAskBucket();
		assertEquals(1, asks.size());
		assertEquals(5l, buckets.get(901l).getVolume());
		assertEquals(market.getLastPrice(), 901l);
	}
}
