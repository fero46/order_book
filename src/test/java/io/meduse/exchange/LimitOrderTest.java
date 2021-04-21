package io.meduse.exchange;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import io.meduse.data.Bucket;
import io.meduse.messages.OrderMessage;
import io.meduse.processors.LimitOrder;

public class LimitOrderTest {

  @Test
  public void testLimitSell() {
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

    price = new BigDecimal(801l);
    bid = new Order("5", "btcusdt", price, volume, Order.LIMIT_ORDER, Order.BID);
    market.add(bid);

    Order marketOrder = new Order("4", "btcusdt", new BigDecimal(895l), new BigDecimal(25l), Order.LIMIT_ORDER, Order.ASK);

    List<OrderMessage> messages = new LimitOrder(market, marketOrder).process();

    assertEquals(3, messages.size());
    for (int i = 0; i < 3; i++) {
      assertEquals("io.meduse.messages.OrderMatch", messages.get(i).getClass().getName());
    }
    for (OrderMessage message : messages) {
      System.out.println(message.tickerMessage());
    }

    NavigableSet<BigDecimal> bids = market.getBids();
    Map<BigDecimal, Bucket> buckets = market.getBidBucket();

    assertEquals(2, bids.size());
    assertEquals(5l, buckets.get(new BigDecimal(895l)).getVolume().longValue());
    assertEquals(market.getLastPrice().compareTo(new BigDecimal(895l)), 0);
  }

}
