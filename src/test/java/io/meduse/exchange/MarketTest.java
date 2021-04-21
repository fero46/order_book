package io.meduse.exchange;

import java.math.BigDecimal;
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
    BigDecimal volume = new BigDecimal(10l);
    BigDecimal price = new BigDecimal(895l);
    Order bid = new Order("1", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
    market.add(bid);
    bid = new Order("2", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
    market.add(bid);
    price = new BigDecimal(855l);
    bid = new Order("3", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
    market.add(bid);
    price = new BigDecimal(865l);
    bid = new Order("4", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
    market.add(bid);
    bid = new Order("5", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
    market.add(bid);
    price = new BigDecimal(895l);
    bid = new Order("2", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
    market.add(bid);
    price = new BigDecimal(1055l);
    bid = new Order("3", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
    market.add(bid);
    price = new BigDecimal(1055l);
    bid = new Order("4", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
    market.add(bid);
    bid = new Order("5", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
    market.add(bid);
    System.out.println(market.getBids());
    System.out.println(market.getAsks());

    NavigableSet<BigDecimal> buyOrders = market.getBids().headSet(new BigDecimal(870l), true);
    System.out.println(buyOrders);
    NavigableSet<BigDecimal> sellOrders = market.getAsks().tailSet(new BigDecimal(970l), true);
    System.out.println(sellOrders);
  }

  @Test
  void testMixedOrders() {
    OrderBook market = defaultOrderBook();
    System.out.println(market.getMarketDepth());
    Order ask = new Order("23454213", "btcusdt", new BigDecimal(8l), new BigDecimal(4l),
        Order.LIMIT_ORDER, Order.ASK);
    new LimitOrder(market, ask).process();
    System.out.println(market.getMarketDepth());
    Order bid = new Order("134567", "btcusdt", new BigDecimal(0l), new BigDecimal(8l),
        Order.MARKET_ORDER, Order.BID);
    new MarketOrder(market, bid).process();
    System.out.println(market.getMarketDepth());
  }

  @Test
  void testMarketApi() {
    Random r = new Random();

    int id = 0;
    String marketString = "btcusdt";
    BigDecimal price;
    BigDecimal volume;
    OrderBook market = new OrderBook();

    int min = 1;
    int max = 10;
    int volmin = 1;
    int volmax = 10;

    for (int i = 0; i < 100; i++) {
      id++;
      price = new BigDecimal(ThreadLocalRandom.current().nextLong(min, max));
      volume = new BigDecimal(ThreadLocalRandom.current().nextLong(volmin, volmax));
      Order bid = new Order(id + "", marketString, price, volume, Order.LIMIT_ORDER, Order.BID);
      new LimitOrder(market, bid).process();
      id++;
      price = new BigDecimal(ThreadLocalRandom.current().nextLong(min, max));
      volume = new BigDecimal(ThreadLocalRandom.current().nextLong(volmin, volmax));
      Order ask = new Order(id + "", marketString, price, volume, Order.LIMIT_ORDER, Order.ASK);
      new LimitOrder(market, ask).process();
    }

    System.out.println(market.getLastPrice());
    System.out.println(market.getMarketDepth());
  }

  private OrderBook defaultOrderBook() {
    OrderBook market = new OrderBook();

    String marketString = "btcusdt";
    BigDecimal[][] asks = { { new BigDecimal(10l), new BigDecimal(2l) },
        { new BigDecimal(11l), new BigDecimal(3l) }, { new BigDecimal(12l), new BigDecimal(4l) },
        { new BigDecimal(13l), new BigDecimal(5l) } };
    BigDecimal[][] bids = { { new BigDecimal(9l), new BigDecimal(2l) },
        { new BigDecimal(8l), new BigDecimal(3l) }, { new BigDecimal(7l), new BigDecimal(4l) },
        { new BigDecimal(6l), new BigDecimal(5l) } };
    int id = 0;

    for (int i = 0; i < asks.length; i++) {
      id++;
      BigDecimal price = asks[i][0];
      BigDecimal volume = asks[i][1];
      Order ask = new Order(id + "", marketString, price, volume, Order.LIMIT_ORDER, Order.ASK);
      new LimitOrder(market, ask).process();
    }

    for (int i = 0; i < bids.length; i++) {
      id++;
      BigDecimal price = bids[i][0];
      BigDecimal volume = bids[i][1];
      Order bid = new Order(id + "", marketString, price, volume, Order.LIMIT_ORDER, Order.BID);
      new LimitOrder(market, bid).process();
    }
    return market;
  }

}
