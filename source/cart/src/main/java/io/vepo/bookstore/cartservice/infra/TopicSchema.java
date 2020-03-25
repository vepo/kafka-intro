package io.vepo.bookstore.cartservice.infra;

import static java.util.Objects.isNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

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

    private TopicSchema() {
    }

    public Class<?> resolveClass(String topic) {
        return Stream.of(Topics.values())
                     .filter(t -> t.topicName().equals(topic)).findFirst()
                     .orElseThrow(() -> new IllegalStateException("Topic schema not found! topic=" + topic))
                     .schemaClass();
    }
}
