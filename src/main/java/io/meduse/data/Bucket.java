package io.meduse.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.meduse.exchange.Order;

public class Bucket {

  List<String> ids = new ArrayList<String>();
  Map<String, Order> orderMap = new HashMap<String, Order>();
  BigDecimal volume = BigDecimal.ZERO;

  public boolean add(Order order) {
//    System.out.println("ORDER VOLUMENT " + order.getVolume());
//    System.out.println("volume " + volume);
    volume = volume.add(order.getVolume());
    orderMap.put(order.getId(), order);
    return ids.add(order.getId());
  }

  public boolean remove(Order order) {
    if (orderMap.containsKey(order.getId())) {
      volume = volume.subtract(order.getVolume());
      orderMap.remove(order.getId());
      return ids.remove(order.getId());
    } else {
      return false;
    }
  }

  public boolean reduceVolume(String id, BigDecimal volume) {
    Order order = orderMap.get(id);
    if (order.getVolume().compareTo(volume) >= 0) {
      this.volume = this.volume.subtract(volume);
      order.setVolume((order.getVolume().subtract(volume)));
      return true;
    } else {
      return false;
    }
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public boolean contains(Order order) {
    return orderMap.containsKey(order.getId());
  }

  public int size() {
    return ids.size();
  }

  public List<String> getIds() {
    return ids;
  }

  public Order getOrder(String id) {
    return orderMap.get(id);
  }

}
