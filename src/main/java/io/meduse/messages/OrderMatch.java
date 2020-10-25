package io.meduse.messages;

public class OrderMatch implements OrderMessage {

	Long price;
	private String takerId;
	private String makerId;
	private Long volume;

	public OrderMatch(String takerId, String makerId, Long price, Long volume) {
		this.takerId = takerId;
		this.makerId = makerId;
		this.price = price;
		this.volume = volume;
	}

	@Override
	public boolean success() {
		return true;
	}

	public Long getVolume() {
		return volume;
	}

	@Override
	public String makerId() {
		// TODO Auto-generated method stub
		return makerId;
	}

	@Override
	public String takerId() {
		// TODO Auto-generated method stub
		return takerId;
	}

	@Override
	public String tickerMessage() {
		return "Order with id " + takerId + " takes Order with id " + makerId + " " + volume + " for the price "
				+ price;
	}

}
