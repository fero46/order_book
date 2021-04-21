package io.meduse.exchange;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import io.meduse.data.Bucket;

public class OrderBook {
  BigDecimal lastPrice;

  final NavigableSet<BigDecimal> bidPrices = new TreeSet<BigDecimal>(Collections.reverseOrder());
  final NavigableSet<BigDecimal> askPrices = new TreeSet<BigDecimal>();

  final HashMap<BigDecimal, Bucket> bid = new HashMap<BigDecimal, Bucket>();
  final HashMap<BigDecimal, Bucket> ask = new HashMap<BigDecimal, Bucket>();

  public void add(Order order) {
    if (order.getDirection() == Order.ASK) {
      add(order, ask, askPrices);
    } else if (order.getDirection() == Order.BID) {
      add(order, bid, bidPrices);
    }
  }

  private void add(Order order, HashMap<BigDecimal, Bucket> orderMap,
      NavigableSet<BigDecimal> orderPrices) {
    Bucket orders = orderMap.get(order.getPrice());
    if (orders == null) {
      orders = new Bucket();
      orderMap.put(order.getPrice(), orders);
      orderPrices.add(order.getPrice());
    }
    orders.add(order);
  }

  public NavigableSet<BigDecimal> getBids() {
    return bidPrices;
  }

  public NavigableSet<BigDecimal> getAsks() {
    return askPrices;
  }

  public void remove(Order order) {
    if (order.getDirection() == Order.ASK) {
      remove(order, ask, askPrices);
    } else if (order.getDirection() == Order.BID) {
      remove(order, bid, bidPrices);
    }
  }

  private void remove(Order order, HashMap<BigDecimal, Bucket> book, Set<BigDecimal> prices) {
    if (book.get(order.getPrice()) != null) {
      Bucket list = book.get(order.getPrice());
      if (list.contains(order)) {
        list.remove(order);
      }
      if (list.size() == 0) {
        book.remove(order.getPrice());
        prices.remove(order.getPrice());
      }
    }
  }

  public void setPrice(BigDecimal price) {
    lastPrice = price;
  }

  public BigDecimal getLastPrice() {
    return lastPrice;
  }

  public HashMap<BigDecimal, Bucket> getBidBucket() {
    return bid;
  }

  public HashMap<BigDecimal, Bucket> getAskBucket() {
    return ask;
  }

  public MarketDepth getMarketDepth() {
    return new MarketDepth(this);
  }

}
