package io.meduse.exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
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
    BigDecimal volume = new BigDecimal(10l);
    BigDecimal price = new BigDecimal(895l);
    Order bid = new Order("1", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
    market.add(bid);
    bid = new Order("2", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
    market.add(bid);
    price = new BigDecimal(901l);
    bid = new Order("3", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
    market.add(bid);
    Order marketOrder = new Order("4", "btcusdt", BigDecimal.ZERO, new BigDecimal(25l),
        Order.MARKET_ORDER, Order.ASK);
    List<OrderMessage> messages = new MarketOrder(market, marketOrder).process();

    for (int i = 0; i < 3; i++) {
      assertEquals("io.meduse.messages.OrderMatch", messages.get(i).getClass().getName());
    }
    assertEquals(3, messages.size());

    for (OrderMessage message : messages) {
      System.out.println(message.tickerMessage());
    }

    NavigableSet<BigDecimal> bids = market.getBids();
    Map<BigDecimal, Bucket> buckets = market.getBidBucket();
    assertEquals(1, bids.size());
    assertEquals(5l, buckets.get(new BigDecimal(895l)).getVolume().longValue());
    assertEquals(market.getLastPrice().longValue(), 895l);
  }

  @Test
  void testBidOrder() {
    System.out.println("Start Market Buy");
    OrderBook market = new OrderBook();
    BigDecimal volume = new BigDecimal(10l);
    BigDecimal price = new BigDecimal(895l);
    Order ask = new Order("1", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
    market.add(ask);
    ask = new Order("2", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
    market.add(ask);
    price = new BigDecimal(901l);
    ask = new Order("3", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.ASK);
    market.add(ask);
    Order marketOrder = new Order("4", "btcusdt", BigDecimal.ZERO, new BigDecimal(25l),
        Order.MARKET_ORDER, Order.BID);
    List<OrderMessage> messages = new MarketOrder(market, marketOrder).process();

    assertEquals(3, messages.size());
    for (int i = 0; i < 3; i++) {
      assertEquals("io.meduse.messages.OrderMatch", messages.get(i).getClass().getName());
    }
    for (OrderMessage message : messages) {
      System.out.println(message.tickerMessage());
    }

    NavigableSet<BigDecimal> asks = market.getAsks();
    Map<BigDecimal, Bucket> buckets = market.getAskBucket();
    assertEquals(1, asks.size());
    assertEquals(5l, buckets.get(new BigDecimal(901l)).getVolume().longValue());
    assertEquals(market.getLastPrice().longValue(), 901l);
  }
}
