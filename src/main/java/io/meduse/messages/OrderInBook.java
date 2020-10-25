package io.meduse.messages;

import io.meduse.exchange.Order;

public class OrderInBook implements OrderMessage {

	private Order order;

	public OrderInBook(Order order) {
		this.order = order;
	}

	@Override
	public boolean success() {
		return true;
	}

	@Override
	public String makerId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String takerId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String tickerMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
