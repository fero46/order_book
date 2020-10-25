package io.meduse.starter;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import io.meduse.data.HttpSuccess;
import io.meduse.data.MarketData;
import io.meduse.data.MarketDetail;
import io.meduse.exchange.MarketManager;

public class WebService implements Runnable {

	private final MarketManager marketManager;

	public WebService(MarketManager marketManager) {
		this.marketManager = marketManager;
	}

	public void init() {
		run();
		//new Thread(this).run();
	}

	@Override
	public void run() {
		System.out.println("Start Port");
		port(8080);
		get("/market", (request, response) -> {
			return new MarketData(marketManager);
		}, new JsonTransformer());
		get("/market/:id", (request, response) -> {
			String id = request.params(":id");
			return new MarketDetail(marketManager, id);
		}, new JsonTransformer());
		post("/market", (request, response) -> {
			String marketId = request.queryParams("id");
			this.marketManager.addMarket(marketId);
			return new HttpSuccess();
		}, new JsonTransformer());
		delete("/market", (request, response) -> {
			String marketId = request.params("id");
			this.marketManager.removeMarket(marketId);
			return new HttpSuccess();
		}, new JsonTransformer());
	}

}
