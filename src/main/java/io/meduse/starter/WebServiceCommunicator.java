package io.meduse.starter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.LinkedTransferQueue;

import io.meduse.data.ExchangeConfiguration;
import io.meduse.messages.OrderMessage;

public class WebServiceCommunicator implements Runnable {

  private final LinkedTransferQueue<OrderMessage> queue;
  private final String host;
  private String path;
  private URI uri;

  public WebServiceCommunicator(LinkedTransferQueue<OrderMessage> queue) {
    this.queue = queue;
    this.host = ExchangeConfiguration.CALL_BACK_HOST + ":" + ExchangeConfiguration.CALL_BACK_PORT;
    this.path = ExchangeConfiguration.CALL_BACK_PATH;
    this.uri = URI.create(this.host + this.path);
  }

  public void init() {
    Thread newThread = new Thread(this);
    newThread.run();
  }

  @Override
  public void run() {
    try {
      while (true) {
        consume(queue.take());
      }
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
  }

  private void consume(OrderMessage message) {
    String data = message.to_json_string();
    try {
      post(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void post(String data) throws Exception {
    HttpClient client = HttpClient.newBuilder().build();
    HttpRequest request = HttpRequest.newBuilder().uri(this.uri).POST(BodyPublishers.ofString(data))
        .build();

    HttpResponse<?> response = client.send(request, BodyHandlers.discarding());
  }

}
