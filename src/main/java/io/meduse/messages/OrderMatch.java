package io.meduse.messages;

import java.math.BigDecimal;

import com.google.gson.JsonObject;

import io.meduse.data.ExchangeConfiguration;

public class OrderMatch implements OrderMessage {

  BigDecimal price;
  private String takerId;
  private String makerId;
  private BigDecimal volume;

  public OrderMatch(String takerId, String makerId, BigDecimal price, BigDecimal volume) {
    this.takerId = takerId;
    this.makerId = makerId;
    this.price = price;
    this.volume = volume;
  }

  @Override
  public boolean success() {
    return true;
  }

  public BigDecimal getVolume() {
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
    return "Order with id " + takerId + " takes Order with id " + makerId + " " + volume
        + " for the price " + price;
  }
  
  @Override
  public String to_json_string() {
    JsonObject json = new JsonObject();
    json.addProperty("id", takerId());
    json.addProperty("success", true);
    json.addProperty("action", "match");
    json.addProperty("maker", makerId());
    json.addProperty("volume", getVolume());
    json.addProperty("secret", ExchangeConfiguration.CALL_BACK_SECRET);
    return json.toString();
  }

}
