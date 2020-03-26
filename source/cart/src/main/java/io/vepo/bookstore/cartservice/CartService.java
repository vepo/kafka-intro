package io.vepo.bookstore.cartservice;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vepo.bookstore.cartservice.infra.KafkaFactory;
import io.vepo.bookstore.cartservice.infra.Topics;

public class CartService implements AutoCloseable, Runnable {
    private static final String GROUP_ID_CART_RESERVE_PROCESSOR = "cart.reserve.processor";
    private static final String GROUP_ID_CART_RESERVED_PROCESSOR = "cart.reserved.processor";
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    public static void main(String[] argv) {
        try (var service = new CartService()) {
            service.run();
        }
    }

    private Producer<String, Object> producer;
    private CartManager cartManager;
    private ExecutorService threadPool;
    private Map<String, CartProductReserve> messages;

    private CartService() {
        this.threadPool = Executors.newFixedThreadPool(2);
        this.producer = KafkaFactory.instance().createProducer();
        this.cartManager = CartManager.instance();
        this.messages = new HashMap<>();
    }

    private void processStockItemReserved(ConsumerRecord<String, StockItemReserved> message) {
        logger.info("Message received from partition={} with offset={}", message.partition(), message.offset());
        var reserved = message.value();
        var userId = message.key();
        logger.info("Received key={} tvalue={}", userId, reserved);

        
        logger.info("Stock Reserved Event!\n\nevent={}\n\n", reserved);

        CartProductReserve request = this.messages.remove(reserved.id());

        requireNonNull(request, "Request not found! Stupid database!");

        var cartReserved = new CartProductReserved(request.id(), reserved.status(), reserved.quantity());

        logger.info("Sending Cart Item reserved event!\n\nevent={}\n\n", cartReserved);

        producer.send(new ProducerRecord<>(Topics.CART_PRODUCT_RESERVED.topicName(),
                                           request.userId(),
                                           cartReserved),
                      this::logSentMessage);

    }

    private void processCartReserveProduct(ConsumerRecord<String, CartProductReserve> message) {
        logger.info("Message received from partition={} with offset={}", message.partition(), message.offset());
        var reserve = message.value();
        var userId = message.key();
        logger.info("Received key={} tvalue={}", userId, reserve);

        logger.info("Cart Reserved Event!\n\nevent={}\n\n", reserve);

        this.messages.put(reserve.id(), reserve);

        var cart = cartManager.getActiveCart(userId);
        cart.addProduct(reserve.productId(), reserve.quantity());

        var stockReserveEvent = new StockItemReserve(reserve.id(), cart.cartId(), reserve.productId(), reserve.quantity());

        logger.info("Sending Stock Item reserve event!\n\nevent={}\n\n", stockReserveEvent);

		producer.send(new ProducerRecord<>(Topics.STOCK_ITEM_RESERVE.topicName(),
                                           cart.cartId(),
                                           stockReserveEvent),
                      this::logSentMessage);
    }

    private void logSentMessage(RecordMetadata metadata, Exception exception) {
        if (isNull(exception)) {
            logger.info("Message sent! metadata={}", metadata);
        } else {
            logger.error("Could not send message!", exception);
        }
    }

    @Override
    public void run() {
        var futures = asList(this.threadPool.submit(()-> KafkaFactory.instance().startConsumer(GROUP_ID_CART_RESERVE_PROCESSOR, Topics.CART_PRODUCT_RESERVE.topicName(), this::processCartReserveProduct)),
                             this.threadPool.submit(()-> KafkaFactory.instance().startConsumer(GROUP_ID_CART_RESERVED_PROCESSOR, Topics.STOCK_ITEM_RESERVED.topicName(), this::processStockItemReserved)));

        logger.info("Cart Service Started!!!!!");

        futures.forEach(future-> {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Erro!", e);
                }
            });;
    }

    @Override
    public void close() {
        producer.close();
        threadPool.shutdownNow();
    }

}
