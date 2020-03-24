package io.vepo.bookstore.stockservice;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vepo.bookstore.stockservice.infra.KafkaFactory;

public class StockService implements AutoCloseable {
	private static final Logger logger = LoggerFactory.getLogger(StockService.class);
	private ExecutorService threadPool;
	private KafkaProducer<String, StockItemReserved> producer;

	public StockService() {
		this.threadPool = Executors.newFixedThreadPool(1);
		this.producer = KafkaFactory.instance().<String, StockItemReserved>createProducer();
	}

	public static void main(String[] argv) {
		try (var service = new StockService()) {
			service.start();
		}
	}

	private void processItemReserveMessage(ConsumerRecord<String, StockItemReserve> message) {
		logger.info("Message received from partition={} with offset={}", message.partition(), message.offset());

		var reserve = message.value();
		var cartId = message.key();
		logger.info("key={}\tvalue={}", cartId, reserve);
		StockManager.instance().reserveItem(cartId, reserve.productId(), reserve.quantity())
				.ifPresentOrElse(order -> logger.info("Reserved! {}", order), () -> logger.info("NO!!!!"));
	}

	public void start() {
		var futures = new ArrayList<Future<?>>();
		futures.add(threadPool.submit(() -> KafkaFactory.instance().<String, StockItemReserve>startConsumer(
				"stock.item.reserve.processor", "stock.item.reserve", this::processItemReserveMessage)));

		futures.forEach(result -> {
			try {
				result.get();
			} catch (InterruptedException | ExecutionException e) {
				logger.error("Erro!", e);
			}
		});
	}

	@Override
	public void close() {
		this.producer.close();
		this.threadPool.shutdownNow();
	}
}