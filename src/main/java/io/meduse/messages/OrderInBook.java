package io.meduse.messages;

import com.google.gson.JsonObject;

import io.meduse.data.ExchangeConfiguration;
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
		return order.getId();
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
	
  @Override
  public String to_json_string() {
    JsonObject json = new JsonObject();
    json.addProperty("id", makerId());
    json.addProperty("success", true);
    json.addProperty("action", "order");
    json.addProperty("secret", ExchangeConfiguration.CALL_BACK_SECRET);
    return json.toString();
  }

}
