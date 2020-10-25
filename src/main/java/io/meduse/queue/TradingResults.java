package io.meduse.queue;

import java.util.List;

import io.meduse.messages.OrderMessage;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;

public class TradingResults {

	private static final String TRADING_RESULT_QUEUE = "TRADE_RESULT";

	private SqsClient sqsClient;

	public TradingResults(SqsClient sqsClient) {
		this.sqsClient = sqsClient;
		CreateQueueRequest queueName = CreateQueueRequest.builder().queueName(TRADING_RESULT_QUEUE).build();
		try {
			sqsClient.createQueue(queueName);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());

			System.out.println("queue exists");
		}
	}

	public void sendResults(List<OrderMessage> processOrder) {
		
	}

}
