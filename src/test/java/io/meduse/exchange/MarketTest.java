package io.meduse.exchange;

import java.util.NavigableSet;
import java.util.Random;

import org.junit.jupiter.api.Test;

import io.meduse.processors.LimitOrder;
import io.meduse.processors.MarketOrder;
import io.netty.util.internal.ThreadLocalRandom;

class MarketTest {

	@Test
	void test() {
		OrderBook market = new OrderBook();
		Long price = 895l;
		Long volume = 10l;
		Order bid = new Order("1", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
		market.add(bid);
		bid = new Order("2", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
		market.add(bid);
		price = 855l;
		bid = new Order("3", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
		market.add(bid);
		price = 865l;
		bid = new Order("4", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
		market.add(bid);
		bid = new Order("5", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
		market.add(bid);
		price = 895l;
		bid = new Order("2", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
		market.add(bid);
		price = 1055l;
		bid = new Order("3", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
		market.add(bid);
		price = 1055l;
		bid = new Order("4", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
		market.add(bid);
		bid = new Order("5", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
		market.add(bid);
		System.out.println(market.getBids());
		System.out.println(market.getAsks());

		NavigableSet<Long> buyOrders = market.getBids().headSet(870l, true);
		System.out.println(buyOrders);
		NavigableSet<Long> sellOrders = market.getAsks().tailSet(970l, true);
		System.out.println(sellOrders);
	}

	@Test
	void testMixedOrders() {
		OrderBook market = defaultOrderBook();
		System.out.println(market.getMarketDepth());
		Order ask = new Order("23454213", "btcusdt", 8l, 4l, Order.LIMIT_ORDER, Order.ASK);
		new LimitOrder(market, ask).process();
		System.out.println(market.getMarketDepth());
		Order bid = new Order("134567", "btcusdt", 0l, 8l, Order.MARKET_ORDER, Order.BID);
		new MarketOrder(market, bid).process();
		System.out.println(market.getMarketDepth());
	}

	@Test
	void testMarketApi() {
		Random r = new Random();

		int id = 0;
		String marketString = "btcusdt";
		Long price;
		Long volume;
		OrderBook market = new OrderBook();

		int min = 1;
		int max = 10;
		int volmin = 1;
		int volmax = 10;

		for (int i = 0; i < 100; i++) {
			id++;
			price = ThreadLocalRandom.current().nextLong(min, max);
			volume = ThreadLocalRandom.current().nextLong(volmin, volmax);
			Order bid = new Order(id + "", marketString, price, volume, Order.LIMIT_ORDER, Order.BID);
			new LimitOrder(market, bid).process();
			id++;
			price = ThreadLocalRandom.current().nextLong(min, max);
			volume = ThreadLocalRandom.current().nextLong(volmin, volmax);
			Order ask = new Order(id + "", marketString, price, volume, Order.LIMIT_ORDER, Order.ASK);
			new LimitOrder(market, ask).process();
		}

		System.out.println(market.getLastPrice());
		System.out.println(market.getMarketDepth());
	}

	private OrderBook defaultOrderBook() {
		OrderBook market = new OrderBook();

		String marketString = "btcusdt";
		Long[][] asks = { { 10l, 2l }, { 11l, 3l }, { 12l, 4l }, { 13l, 5l } };
		Long[][] bids = { { 9l, 2l }, { 8l, 3l }, { 7l, 4l }, { 6l, 5l } };
		int id = 0;

		for (int i = 0; i < asks.length; i++) {
			id++;
			Long price = asks[i][0];
			Long volume = asks[i][1];
			Order ask = new Order(id + "", marketString, price, volume, Order.LIMIT_ORDER, Order.ASK);
			new LimitOrder(market, ask).process();
		}

		for (int i = 0; i < bids.length; i++) {
			id++;
			Long price = bids[i][0];
			Long volume = bids[i][1];
			Order bid = new Order(id + "", marketString, price, volume, Order.LIMIT_ORDER, Order.BID);
			new LimitOrder(market, bid).process();
		}
		return market;
	}

}
