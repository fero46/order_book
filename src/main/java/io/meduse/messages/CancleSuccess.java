package io.meduse.messages;

public class CancleSuccess implements OrderMessage {

	String id;

	public CancleSuccess(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
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
