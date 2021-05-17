package io.meduse.messages;

import com.google.gson.JsonObject;

import io.meduse.data.ExchangeConfiguration;
import io.meduse.exchange.Order;

public class MarketPlacementError implements OrderMessage {

  private Order order;

  public MarketPlacementError(Order order) {
    this.order = order;
  }
  
  @Override
  public boolean success() {
    return false;
  }

  @Override
  public String makerId() {
    return order.getId();
  }

  @Override
  public String takerId() {
    return null;
  }

  @Override
  public String tickerMessage() {
    return null;
  }

  @Override
  public String to_json_string() {
    JsonObject json = new JsonObject();
    json.addProperty("id", makerId());
    json.addProperty("success", false);
    json.addProperty("action", "order");
    json.addProperty("secret", ExchangeConfiguration.CALL_BACK_SECRET);
    return json.toString();
  }

}
