package io.meduse.data;

import io.meduse.exchange.MarketDepth;
import io.meduse.exchange.MarketManager;
import io.meduse.exchange.OrderBook;

public class MarketDetail {

	private final Long lastPrice;
	private final MarketDepth depth;

	public MarketDetail(MarketManager marketManager, String id) {
		OrderBook market = marketManager.getMarket(id);
		this.lastPrice = market.getLastPrice() != null ? market.getLastPrice() : 0l;
		this.depth = market.getMarketDepth();
	}

	public Long getLastPrice() {
		return lastPrice;
	}

	public MarketDepth getDepth() {
		return depth;
	}

}
