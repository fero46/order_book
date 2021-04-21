package io.meduse.processors;

import java.util.List;

import io.meduse.exchange.Order;
import io.meduse.exchange.OrderBook;
import io.meduse.messages.OrderMessage;

public class CancleOrder implements Processor {

	private OrderBook market;
	private Order order;

	public CancleOrder(OrderBook market, Order order) {
		this.market = market;
		this.order = order;
	}

	@Override
  public List<OrderMessage> process() {
		market.remove(order);
		return null;
	}

}
