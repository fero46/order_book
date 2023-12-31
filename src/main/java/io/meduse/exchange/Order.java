package io.meduse.exchange;

import java.math.BigDecimal;
import java.util.HashMap;

import com.google.gson.Gson;

public class Order {

  public static final int ASK = 10;
  public static final int BID = 20;

  public static final int LIMIT_ORDER = 100;
  public static final int MARKET_ORDER = 200;
  public static final int CANCLE_ORDER = 300;

  private BigDecimal price;
  private BigDecimal volume;
  private String id;
  private String market;
  private int type;
  private int direction;
  private boolean marketOrder;

  public Order(String id, String market, BigDecimal price, BigDecimal volume, int type,
      int direction) {
    this(id, market, price, volume, type, direction, false);
  }

  public Order(String id, String market, BigDecimal price, BigDecimal volume, int type,
      int direction, boolean marketOrder) {
    this.id = id;
    this.market = market;
    this.price = price;
    this.type = type;
    this.direction = direction;
    this.volume = volume;
    this.marketOrder = marketOrder;
  }

  public boolean getMarketOrder() {
    return marketOrder;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public int getType() {
    return type;
  }

  public int getDirection() {
    return direction;
  }

  public String getMarket() {
    return market;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public String getId() {
    return id;
  }

  public static Order fromMessageBody(String body) {

    @SuppressWarnings("unchecked")
    HashMap<String, String> map = new Gson().fromJson(body, HashMap.class);
    Order o = null;
    try {

      BigDecimal price = new BigDecimal(map.get("price"));
      BigDecimal volume = new BigDecimal(map.get("volume"));
      int type = -1;
      int direction = -1;
      String id = map.get("id");
      String market = map.get("market");
      boolean marketOrder = false;

      if ("limit".equals(map.get("type"))) {
        type = Order.LIMIT_ORDER;
      } else if ("market".equals(map.get("type"))) {
        type = Order.MARKET_ORDER;
      }

      if ("ask".equals(map.get("direction"))) {
        direction = Order.ASK;
      } else if ("bid".equals(map.get("direction"))) {
        direction = Order.BID;
      }

      if (map.containsKey("market_maker")) {
        marketOrder = map.get("market_maker") == "true";
      }

      if (direction != -1 && type != -1 && volume.compareTo(BigDecimal.ZERO) > 0
          && (type == Order.MARKET_ORDER || price.compareTo(BigDecimal.ZERO) > 0)) {
        Order order = new Order(id, market, price, volume, type, direction, marketOrder);
        return order;
      }

    } catch (Exception e) {
      // e.printStackTrace();
    }
    return null;
  }

  public void setVolume(BigDecimal volume) {
    this.volume = volume;
  }

  @Override
  public String toString() {
    return "{\"market\":\"" + market + "\"," + "\"price\":\"" + price + "\"," + "\"volume\":\""
        + volume + "\"," + "\"id\":\"" + id + "\"," + "\"type\":\"" + type + "\","
        + "\"direction\":\"" + direction + "\"}";
  }

}
