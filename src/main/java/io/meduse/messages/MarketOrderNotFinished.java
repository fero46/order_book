package io.meduse.messages;

import com.google.gson.JsonObject;

import io.meduse.data.ExchangeConfiguration;

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
	
  @Override
  public String to_json_string() {
    JsonObject json = new JsonObject();
    json.addProperty("id", makerId());
    json.addProperty("success", false);
    json.addProperty("action", "market");
    json.addProperty("secret", ExchangeConfiguration.CALL_BACK_SECRET);
    return json.toString();
  }

}
