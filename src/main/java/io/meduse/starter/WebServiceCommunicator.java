package io.meduse.starter;

import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import io.meduse.data.ExchangeConfiguration;
import io.meduse.messages.OrderMessage;

public class WebServiceCommunicator implements Runnable {

  private final BlockingQueue<OrderMessage> queue;
  private final String host;
  private String path;
  private URI uri;

  public WebServiceCommunicator(BlockingQueue<OrderMessage> queue) {
    this.queue = queue;
    this.host = ExchangeConfiguration.CALL_BACK_HOST + ":" + ExchangeConfiguration.CALL_BACK_PORT;
    this.path = ExchangeConfiguration.CALL_BACK_PATH;
    this.uri = URI.create(this.host + this.path);
  }

  public void init() {
    Thread newThread = new Thread(this);
    newThread.start();
  }

  @Override
  public void run() {
    try {
      System.out.println("Starting Call Back Service");
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
      System.out.println("Post Data " + data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void post(String data) throws Exception {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(uri.toString());
    StringEntity entity = new StringEntity(data);
    httpPost.setEntity(entity);
    CloseableHttpResponse response = client.execute(httpPost);
    client.close();
    if (response.getStatusLine().getStatusCode() != 200) {
      throw new Exception("Error CALLBACK doesn't return 200 returns : "
          + response.getStatusLine().getStatusCode());
    }
    
  }

}
