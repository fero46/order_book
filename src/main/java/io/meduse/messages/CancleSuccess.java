package io.meduse.messages;

import com.google.gson.JsonObject;

import io.meduse.data.ExchangeConfiguration;

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
	
  @Override
  public String to_json_string() {
    JsonObject json = new JsonObject();
    json.addProperty("id", makerId());
    json.addProperty("success", true);
    json.addProperty("action", "cancled");
    json.addProperty("secret", ExchangeConfiguration.CALL_BACK_SECRET);
    return json.toString();
  }

}
