package io.meduse.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.meduse.data.ExchangeConfiguration;
import io.meduse.exchange.Order;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

public class TradingOrders {
	private static final String TRADING_MESSAGING_QUEUE = ExchangeConfiguration.AWS_INCOMMING_QUEUE_NAME;
	
	
	private final SqsClient sqsClient;

	public TradingOrders(SqsClient sqsClient) {
		this.sqsClient = sqsClient;
		if (ExchangeConfiguration.TRY_TO_CREATE_QUEUE == "yes") {
			CreateQueueRequest queueName = CreateQueueRequest.builder().queueName(TRADING_MESSAGING_QUEUE).build();
			try {
				sqsClient.createQueue(queueName);
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
				System.out.println("queue exists");
			}
		}
	}

	public List<Order> getOrders() {
		List<Order> result = new ArrayList<Order>();
		GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder().queueName(TRADING_MESSAGING_QUEUE).build();
		String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();

		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("ReceiveMessageWaitTimeSeconds", "20");

		ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder().queueUrl(queueUrl).build();

		List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
		for (Message message : messages) {
			String body = message.body();
			DeleteMessageRequest deleteMessage = DeleteMessageRequest.builder().queueUrl(queueUrl)
					.receiptHandle(message.receiptHandle()).build();
			sqsClient.deleteMessage(deleteMessage);
			Order order = Order.fromMessageBody(body);
			result.add(order);
		}
		return result;
	}

}
