package io.meduse.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.meduse.exchange.Order;

public class Bucket {

	List<String> ids = new ArrayList<String>();
	Map<String, Order> orderMap = new HashMap<String, Order>();
	Long volume = 0l;

	public boolean add(Order order) {
		volume = volume + order.getVolume();
		orderMap.put(order.getId(), order);
		return ids.add(order.getId());
	}

	public boolean remove(Order order) {
		if (orderMap.containsKey(order.getId())) {
			volume = volume - order.getVolume();
			orderMap.remove(order.getId());
			return ids.remove(order.getId());
		} else {
			return false;
		}
	}

	public boolean reduceVolume(String id, Long volume) {
		Order order = orderMap.get(id);
		if (order.getVolume() >= volume) {
			this.volume = this.volume - volume;
			order.setVolume((order.getVolume() - volume));
			return true;
		} else {
			return false;
		}
	}

	public Long getVolume() {
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
