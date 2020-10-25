package io.meduse.processors;

import java.util.List;

import io.meduse.exchange.Order;
import io.meduse.exchange.OrderBook;
import io.meduse.messages.MarketOrderNotFinished;
import io.meduse.messages.OrderMessage;

public class MarketOrder extends LimitOrder {

	public MarketOrder(OrderBook market, Order order) {
		super(market, order);
	}

	@Override
	public List<OrderMessage> process() {
		List<OrderMessage> result = super.process();

		if (order.getVolume() > 0l) {
			result.add(new MarketOrderNotFinished(order.getId()));
		}

		return result;
	}

	@Override
	protected boolean pricePointExceeded(int priceCompare, Long pricePoint) {
		return false;
	}

	@Override
	protected boolean shouldAddRemainingOrder() {
		return false;
	}
}
