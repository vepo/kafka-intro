package io.vepo.bookstore.cartservice;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vepo.bookstore.cartservice.infra.KafkaFactory;

public class CartService {
	private static final Logger logger = LoggerFactory.getLogger(CartService.class);

	public static void main(String[] argv) {
		var cartManager = CartManager.instance();
		var kafkaFactory = KafkaFactory.instance();
		var threadPool = Executors.newFixedThreadPool(1);
		try (var producer = kafkaFactory.<String, StockReserve>createProducer()) {
			var futures = new ArrayList<Future<Void>>();
			futures.add(threadPool.submit(() -> {
				kafkaFactory.<String, CartReserve>startConsumer("cart.reserve.processor", "cart.reserve.product",
						message -> {

							logger.info("Message received from partition={} with offset={}", message.partition(),
									message.offset());

							var reserve = message.value();
							var userId = message.key();
							logger.info("key={}\tvalue={}", userId, reserve);

							var cart = cartManager.getActiveCart(userId);
							cart.addProduct(reserve.productId(), reserve.quantity());

							producer.send(
									new ProducerRecord<String, StockReserve>("stock.reserve.product", cart.cartId(),
											new StockReserve(cart.cartId(), reserve.productId(), reserve.quantity())),
									(metadata, exception) -> {
										if (isNull(exception)) {
											logger.info("Message sent! metadata={}", metadata);
										} else {
											logger.error("Could not send message!", exception);
										}
									});

						});
				return null;
			}));

			futures.forEach(result -> {
				try {
					result.get();
				} catch (InterruptedException | ExecutionException e) {
					logger.error("Erro!", e);
				}
			});

		}

	}
}
