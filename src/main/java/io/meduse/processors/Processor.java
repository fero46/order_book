package io.meduse.processors;

import java.util.List;

import io.meduse.messages.OrderMessage;

public interface Processor {

	public List<OrderMessage> process();

}
