package io.meduse.exchange;

import java.util.Collections;
import java.util.HashMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import io.meduse.data.Bucket;

public class OrderBook {
	Long lastPrice;

	final NavigableSet<Long> bidPrices = new TreeSet<Long>(Collections.reverseOrder());
	final NavigableSet<Long> askPrices = new TreeSet<Long>();

	final HashMap<Long, Bucket> bid = new HashMap<Long, Bucket>();
	final HashMap<Long, Bucket> ask = new HashMap<Long, Bucket>();

	public void add(Order order) {
		if (order.getDirection() == Order.ASK) {
			add(order, ask, askPrices);
		} else if (order.getDirection() == Order.BID) {
			add(order, bid, bidPrices);
		}
	}

	private void add(Order order, HashMap<Long, Bucket> orderMap, NavigableSet<Long> orderPrices) {
		Bucket orders = orderMap.get(order.getPrice());
		if (orders == null) {
			orders = new Bucket();
			orderMap.put(order.getPrice(), orders);
			orderPrices.add(order.getPrice());
		}
		orders.add(order);
	}

	public NavigableSet<Long> getBids() {
		return bidPrices;
	}

	public NavigableSet<Long> getAsks() {
		return askPrices;
	}

	public void remove(Order order) {
		if (order.getDirection() == Order.ASK) {
			remove(order, ask, askPrices);
		} else if (order.getDirection() == Order.BID) {
			remove(order, bid, bidPrices);
		}
	}

	private void remove(Order order, HashMap<Long, Bucket> book, Set<Long> prices) {
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

	public void setPrice(Long price) {
		lastPrice = price;
	}

	public Long getLastPrice() {
		return lastPrice;
	}

	public HashMap<Long, Bucket> getBidBucket() {
		return bid;
	}

	public HashMap<Long, Bucket> getAskBucket() {
		return ask;
	}

	public MarketDepth getMarketDepth() {
		return new MarketDepth(this);
	}

}
