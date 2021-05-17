package io.meduse.starter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

import io.meduse.data.ExchangeConfiguration;
import io.meduse.exchange.MarketManager;
import io.meduse.exchange.Order;
import io.meduse.messages.OrderMessage;
import io.meduse.queue.TradingOrders;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

public class MessagingClient implements Runnable {

  private final MarketManager marketManager;
  private final BlockingQueue<OrderMessage> queue;

  private SqsClient sqsClient;

  public MessagingClient(MarketManager marketManager, BlockingQueue<OrderMessage> queue) {

    this.marketManager = marketManager;
    this.queue = queue;
    AwsCredentialsProvider credsV2 = StaticCredentialsProvider.create(AwsBasicCredentials
        .create(ExchangeConfiguration.AWS_ACCESS_KEY, ExchangeConfiguration.AWS_ACCESS_PASSWORD));
    URI endpointOverride;
    try {
      endpointOverride = new URI(ExchangeConfiguration.AWS_END_POINT);
      sqsClient = SqsClient.builder().endpointOverride(endpointOverride)
          .region(Region.of(ExchangeConfiguration.AWS_REGION)).credentialsProvider(credsV2).build();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  public void process() {
    Thread newThread = new Thread(this);
    newThread.start();
  }

  @Override
  public void run() {
    while (true) {
      TradingOrders tradingOrders = new TradingOrders(sqsClient);
      List<Order> orders = tradingOrders.getOrders();
      if (orders.size() > 0) {
        System.out.println("New Orders are incomming.");
        System.out.println("Orders: " + orders.size());
      }
      for (Order order : orders) {
        System.out.println("Processing incomming orders");
        List<OrderMessage> processOrders = marketManager.processOrder(order);
        for (OrderMessage message : processOrders) {
          try {
            queue.put(message);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        System.out.println("queue size" + queue.size());
      }
    }
  }
}
