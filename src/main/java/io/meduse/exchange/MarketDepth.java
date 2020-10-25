package io.meduse.exchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableSet;

import com.google.gson.Gson;

import io.meduse.data.Bucket;

public class MarketDepth {

	List<DepthPoint> bid = null;
	List<DepthPoint> ask = null;

	public MarketDepth(OrderBook orderBook) {
		bid = addPoints(orderBook.getBids(), orderBook.getBidBucket());
		ask = addPoints(orderBook.getAsks(), orderBook.getAskBucket());
	}

	private List<DepthPoint> addPoints(NavigableSet<Long> pricePoints, HashMap<Long, Bucket> buckets) {
		List<DepthPoint> result = new ArrayList<DepthPoint>();
		for (Long price : pricePoints) {
			Long volume = buckets.get(price).getVolume();
			result.add(new DepthPoint(price, volume));
		}
		return result;
	}

	public class DepthPoint {
		final public Long price;
		final public Long volume;

		public DepthPoint(Long price, Long volume) {
			this.price = price;
			this.volume = volume;
		}
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
