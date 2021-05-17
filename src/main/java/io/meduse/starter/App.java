/**
 * 
 */
package io.meduse.starter;

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
	  final LinkedTransferQueue<OrderMessage> queue = new LinkedTransferQueue<OrderMessage>();
		MarketManager marketmanager = MarketManager.instance();
		new WebService(marketmanager).init();
		new WebServiceCommunicator(queue).init();
		MessagingClient messagingClient = new MessagingClient(marketmanager, queue);
		messagingClient.process();
	}

}
