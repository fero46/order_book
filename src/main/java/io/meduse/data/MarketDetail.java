package io.meduse.data;

import java.math.BigDecimal;

import io.meduse.exchange.MarketDepth;
import io.meduse.exchange.MarketManager;
import io.meduse.exchange.OrderBook;

public class MarketDetail {

	private final BigDecimal lastPrice;
	private final MarketDepth depth;

	public MarketDetail(MarketManager marketManager, String id) {
		OrderBook market = marketManager.getMarket(id);
		this.lastPrice = market.getLastPrice() != null ? market.getLastPrice() : BigDecimal.ZERO;
		this.depth = market.getMarketDepth();
	}

	public BigDecimal getLastPrice() {
		return lastPrice;
	}

	public MarketDepth getDepth() {
		return depth;
	}

}
