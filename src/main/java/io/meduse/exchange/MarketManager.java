package io.meduse.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.meduse.messages.OrderMessage;
import io.meduse.processors.CancleOrder;
import io.meduse.processors.LimitOrder;
import io.meduse.processors.MarketOrder;

public class MarketManager {

  private static MarketManager instance;
  final Map<String, OrderBook> markets = new HashMap<String, OrderBook>();

  private MarketManager() {
  }

  public void addMarket(String name) {
    if (!markets.containsKey(name)) {
      markets.put(name, new OrderBook());
    }
  }

  public OrderBook getMarket(String name) {
    return markets.get(name);
  }

  public static MarketManager instance() {
    if (instance == null) {
      instance = new MarketManager();
    }
    return instance;
  }

  public List<OrderMessage> processOrder(Order order) {
    OrderBook market = markets.get(order.getMarket());
    if (market != null) {
      System.out.println("Processing Order in Market: " + order.getMarket());
      if (order.getType() == Order.LIMIT_ORDER) {
        return new LimitOrder(market, order).process();
      } else if (order.getType() == Order.MARKET_ORDER) {
        return new MarketOrder(market, order).process();
      } else if (order.getType() == Order.CANCLE_ORDER) {
        return new CancleOrder(market, order).process();
      }
    }
    System.out.println(order.toString());
    return new ArrayList<OrderMessage>();
  }

  public List<String> allMarkets() {
    List<String> result = new ArrayList<String>();
    Set<String> keySet = markets.keySet();
    for (String key : keySet) {
      OrderBook market = markets.get(key);
      BigDecimal lastPrice = market.getLastPrice();
      if (lastPrice == null) {
        lastPrice = BigDecimal.ZERO;
      }
      String info = key + ":" + lastPrice;
      result.add(info);
    }
    return result;
  }

  public void removeMarket(String marketId) {
    if (markets.containsKey(marketId)) {
      markets.remove(marketId);
    }
  }
}
