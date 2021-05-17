/**
 * 
 */
package io.meduse.starter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

import io.meduse.exchange.MarketManager;
import io.meduse.messages.OrderMessage;

/**
 * This is the main starter Class that runs this app.
 * 
 * @author meduse
 *
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	  final BlockingQueue<OrderMessage> queue = new LinkedBlockingQueue<OrderMessage>();
		MarketManager marketmanager = MarketManager.instance();
		new WebService(marketmanager).init();
    new WebServiceCommunicator(queue).init();
		MessagingClient messagingClient = new MessagingClient(marketmanager, queue);
		messagingClient.process();
	}

}
