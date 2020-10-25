/**
 * 
 */
package io.meduse.starter;

import io.meduse.exchange.MarketManager;

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
		MarketManager marketmanager = MarketManager.instance();
		new WebService(marketmanager).init();
		MessagingClient messagingClient = new MessagingClient(marketmanager);
		messagingClient.process();
	}

}
