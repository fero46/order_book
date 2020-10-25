package io.meduse.messages;

public interface OrderMessage {

	public boolean success();
	public String makerId();
	public String takerId();
	public String tickerMessage();
}
