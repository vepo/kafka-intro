package io.vepo.bookstore.stockservice.infra;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaFactory {
    public static final AtomicReference<KafkaFactory> reference = new AtomicReference<>();

    public static KafkaFactory instance() {
        return reference.updateAndGet(ref -> {
            if (isNull(ref)) {
                return new KafkaFactory();
            }
            return ref;
        });
    }

    private KafkaFactory() {
    }

    public <K, V> KafkaProducer<K, V> createProducer() {
        // Configure the Consumer
        var configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaProducer<>(configProperties);
    }

    public <K, V> void startConsumer(String groupId, String topicName, Consumer<ConsumerRecord<K, V>> callback) {
        // Configure the Consumer
        
        var configProperties = new Properties();
        configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        try (var consumer = new KafkaConsumer<K, V>(configProperties)) {
            consumer.subscribe(asList(topicName));
            while (true) {
                consumer.poll(Duration.ofSeconds(1)).forEach(callback);
            }
        }
    }
}
