package io.meduse.data;

import java.util.List;

import io.meduse.exchange.MarketManager;

public class MarketData {

	private List<String> markets;

	public MarketData(MarketManager marketManager) {
		setMarkets(marketManager.allMarkets());
	}

	public List<String> getMarkets() {
		return markets;
	}

	public void setMarkets(List<String> markets) {
		this.markets = markets;
	}

}
