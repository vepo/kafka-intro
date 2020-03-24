package io.vepo.bookstore.stockservice.infra;

import static java.util.Map.entry;
import static java.util.Objects.isNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import io.vepo.bookstore.stockservice.StockItemReserve;

public class TopicSchema {
	public static final AtomicReference<TopicSchema> reference = new AtomicReference<>();

	public static TopicSchema instance() {
		return reference.updateAndGet(ref -> {
			if (isNull(ref)) {
				return new TopicSchema();
			}
			return ref;
		});
	}

	private Map<String, Class<?>> schemas;

	private TopicSchema() {
		schemas = Map.ofEntries(entry("stock.reserve.item", StockItemReserve.class));
	}

	public Class<?> resolveClass(String topic) {
		return schemas.get(topic);
	}
}