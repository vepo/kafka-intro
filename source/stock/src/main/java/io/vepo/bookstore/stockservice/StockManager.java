package io.vepo.bookstore.stockservice;

import static java.util.Objects.isNull;
import static java.util.UUID.randomUUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class StockManager {
	public static final AtomicReference<StockManager> reference = new AtomicReference<>();

	public static StockManager instance() {
		return reference.updateAndGet(ref -> {
			if (isNull(ref)) {
				return new StockManager();
			}
			return ref;
		});
	}

	private Map<String, Order> database;

	private StockManager() {
		database = new HashMap<>();
	}

	public Optional<Order> reserveItem(String cartId, String productId, int quantity) {
		return Optional.of(database.compute(cartId, (__, order) -> {
			if (Objects.isNull(order)) {
				order = new Order(randomUUID().toString(), cartId, new ArrayList<>());
			}
			updateOrder(order, productId, quantity);
			return order;
		}));
	}

	private void updateOrder(Order order, String productId, int quantity) {
		order.items().stream().filter(item -> item.productId().equals(productId)).findFirst().ifPresentOrElse(item -> {
			order.items().remove(item);
			order.items().add(new OrderItem(productId, quantity + item.quantity()));
		}, () -> order.items().add(new OrderItem(productId, quantity)));

	}

}
