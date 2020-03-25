package io.vepo.bookstore.cartservice.infra;

import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDeserializer implements Deserializer<Object> {
    private static final Logger logger = LoggerFactory.getLogger(JsonDeserializer.class);
    private ObjectMapper objectMapper;

    public JsonDeserializer() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, TopicSchema.instance().resolveClass(topic));
        } catch (IOException e) {
            logger.error("Could not deserializer!", e);
            throw new RuntimeException("Could not deserializer!", e);
        }
    }

}
