package io.vepo.bookstore.stockservice;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vepo.bookstore.stockservice.infra.KafkaFactory;
import io.vepo.bookstore.stockservice.infra.Topics;

public class StockService implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    public static void main(String[] argv) {
        try (var service = new StockService()) {
            service.start();
        }
    }

    private KafkaProducer<String, StockItemReserved> producer;
    private ExecutorService threadPool;

    public StockService() {
        this.threadPool = Executors.newFixedThreadPool(1);
        this.producer = KafkaFactory.instance().<String, StockItemReserved>createProducer();
    }

    @Override
    public void close() {
        this.producer.close();
        this.threadPool.shutdownNow();
    }

    private void logSentMessage(RecordMetadata metadata, Exception exception) {
        if (isNull(exception)) {
            logger.info("Message sent! metadata={}", metadata);
        } else {
            logger.error("Could not send message!", exception);
        }
    }

    private void processItemReserveMessage(ConsumerRecord<String, StockItemReserve> message) {
        logger.info("Message received from partition={} with offset={}", message.partition(), message.offset());

        var reserve = message.value();
        var cartId = message.key();
        logger.info("key={}\tvalue={}", cartId, reserve);

        logger.info("Received Reserved Event!\n\nevent={}\n\n", reserve);

        StockManager.instance()
                    .reserveItem(cartId, reserve.productId(), reserve.quantity())
                    .ifPresentOrElse(order -> {

                                        var reservedEvent = new StockItemReserved(reserve.id(), ReserveStatus.CONFIRMED, reserve.quantity());

                                        logger.info("Sending reserved event!\n\nevent={}\n\n", reservedEvent);

                                        producer.send(new ProducerRecord<String, StockItemReserved>(Topics.STOCK_ITEM_RESERVED.topicName(), 
                                                                                                    reserve.cartId(),
                                                                                                    reservedEvent), 
                                                      this::logSentMessage);
                                     }, () -> {

                                        var reservedEvent = new StockItemReserved(reserve.id(), ReserveStatus.FAIL, 0);

                                        logger.info("Sending reserved event!\n\nevent={}\n\n", reservedEvent);


                                        logger.info("No donuts for you! stockItem={}", reserve);
                                        producer.send(new ProducerRecord<String, StockItemReserved>(Topics.STOCK_ITEM_RESERVED.topicName(),
                                                                                                    reserve.cartId(), 
                                                                                                    reservedEvent),
                                                      this::logSentMessage);
                                     });
    }

    public void start() {
        var futures = new ArrayList<Future<?>>();
        futures.add(threadPool.submit(() -> KafkaFactory.instance().<String, StockItemReserve>startConsumer("stock.item.reserve.processor", Topics.STOCK_ITEM_RESERVE.topicName(), this::processItemReserveMessage)));

        logger.info("Stock Service Started!!!!");

        futures.forEach(result -> {
            try {
                result.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Erro!", e);
            }
        });
    }
}