package io.meduse.processors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableSet;

import io.meduse.data.Bucket;
import io.meduse.exchange.Order;
import io.meduse.exchange.OrderBook;
import io.meduse.messages.MarketPlacementError;
import io.meduse.messages.OrderInBook;
import io.meduse.messages.OrderMatch;
import io.meduse.messages.OrderMessage;

public class LimitOrder implements Processor {

  final static int SELL = -1;
  final static int BUY = 1;
  protected OrderBook market;
  protected Order order;
  protected HashMap<BigDecimal, Bucket> buckets;

  public LimitOrder(OrderBook market, Order order) {
    this.market = market;
    this.order = order;
  }

  @Override
  public List<OrderMessage> process() {
//		System.out.println("NEW ORDER");
//		System.out.println("=================================");
//		if (order.getType() == Order.LIMIT_ORDER) {
//			System.out.println("LIMIT ORDER");
//		} else {
//			System.out.println("Market ORDER");
//
//		}
    NavigableSet<BigDecimal> orders = null;
    int priceCompare = 0;
    if (order.getDirection() == Order.BID) {
//			System.out.println("Kauf Order");
      if (order.getType() == Order.LIMIT_ORDER) {
        orders = market.getAsks().headSet(order.getPrice(), true);
      } else {
        orders = market.getAsks();
      }
//			System.out.println("Verkauf: " + orders);
      priceCompare = LimitOrder.BUY;
      buckets = market.getAskBucket();
    } else if (order.getDirection() == Order.ASK) {
//			System.out.println("Verkaufs Order");
      if (order.getType() == Order.LIMIT_ORDER) {
        orders = market.getBids().headSet(order.getPrice(), true);
      } else {
        orders = market.getBids();
      }
//			System.out.println("Kauf: " + orders);
      priceCompare = LimitOrder.SELL;
      buckets = market.getBidBucket();
    }
//		System.out.println("Preis: " + order.getPrice());

    List<OrderMessage> result;
    try {
      result = iterateOrders(orders, priceCompare);
      if (shouldAddRemainingOrder()) {
        market.add(order);
        result.add(new OrderInBook(order));
      }
//      System.out.println("=================================");
    } catch (MarketPlacementException e) {
      result = new ArrayList<OrderMessage>();
      result.add(new MarketPlacementError(order));
    }

    return result;
  }

  /**
   * @return
   */
  protected boolean shouldAddRemainingOrder() {
    return order.getVolume().compareTo(BigDecimal.ZERO) > 0;
  }

  /**
   * @param result
   * @param orders
   * @param priceCompare
   * @throws MarketPlacementException
   */
  protected List<OrderMessage> iterateOrders(NavigableSet<BigDecimal> orders, int priceCompare)
      throws MarketPlacementException {
    List<OrderMessage> result = new ArrayList<OrderMessage>();
    List<Order> orderToRemove = new ArrayList<Order>();

    if (orders != null && orders.size() > 0) {
      for (BigDecimal pricePoint : orders) {
        if (pricePointExceeded(priceCompare, pricePoint)) {
          break;
        }
        Bucket bucket = buckets.get(pricePoint);
        for (String id : bucket.getIds()) {
          Order o = bucket.getOrder(id);
          if (o != null && order.getMarketOrder()) {
            throw new MarketPlacementException();
          }
          if (o.getVolume().compareTo(order.getVolume()) >= 0) {
            bucket.reduceVolume(id, order.getVolume());
            result.add(new OrderMatch(order.getId(), o.getId(), o.getPrice(), order.getVolume()));
            order.setVolume(BigDecimal.ZERO);
            market.setPrice(pricePoint);
          } else if (o.getVolume().compareTo(order.getVolume()) < 0) {
            order.setVolume(order.getVolume().subtract(o.getVolume()));
            result.add(new OrderMatch(order.getId(), o.getId(), o.getPrice(), o.getVolume()));
            bucket.reduceVolume(id, o.getVolume());
            market.setPrice(pricePoint);
          }
          if (o.getVolume().compareTo(BigDecimal.ZERO) == 0) {
            orderToRemove.add(o);
          }
          if (order.getVolume().compareTo(BigDecimal.ZERO) == 0) {
            break;
          }
        }
        if (order.getVolume().compareTo(BigDecimal.ZERO) == 0) {
          break;
        }
      }
    }
    for (Order o : orderToRemove) {
      market.remove(o);
    }
    return result;
  }

  /**
   * @param priceCompare
   * @param pricePoint
   * @return
   */
  protected boolean pricePointExceeded(int priceCompare, BigDecimal pricePoint) {
    return pricePoint.compareTo(order.getPrice()) == priceCompare;
  }

}
