package io.meduse.starter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import io.meduse.data.ExchangeConfiguration;
import io.meduse.exchange.MarketManager;
import io.meduse.exchange.Order;
import io.meduse.messages.OrderMessage;
import io.meduse.queue.TradingOrders;
import io.meduse.queue.TradingResults;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

public class MessagingClient implements Runnable {

	private final MarketManager marketManager;

	private SqsClient sqsClient;

	public MessagingClient(MarketManager marketManager) {

		this.marketManager = marketManager;
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
		run();
		// Thread newThread = new Thread(this);
		// newThread.run();
	}

	@Override
	public void run() {
		while (true) {
			TradingOrders tradingOrders = new TradingOrders(sqsClient);
			TradingResults tradingResults = new TradingResults(sqsClient);
			List<Order> orders = tradingOrders.getOrders();
			for (Order order : orders) {
				List<OrderMessage> processOrder = marketManager.processOrder(order);
				tradingResults.sendResults(processOrder);
			}
		}
	}
}
