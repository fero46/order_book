package io.meduse.messages;

public class MarketOrderNotFinished implements OrderMessage{

	String id;

	public MarketOrderNotFinished(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public boolean success() {
		// TODO Auto-generated method stub
		return false;
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
