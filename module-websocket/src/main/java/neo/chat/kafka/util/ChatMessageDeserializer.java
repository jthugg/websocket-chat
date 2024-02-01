package neo.chat.kafka.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import neo.chat.kafka.model.EventMessageValue;
import neo.chat.persistence.command.entity.CMessage;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
public class ChatMessageDeserializer
        implements Deserializer<EventMessageValue<CMessage>> {

    protected static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    protected static final TypeFactory typeFactory = objectMapper.getTypeFactory();

    @Override
    public EventMessageValue<CMessage> deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(
                    data,
                    typeFactory.constructParametricType(EventMessageValue.class, CMessage.class)
            );
        } catch (IOException e) {
            log.error("문제가 발생했습니다. {}", e.getMessage());
            return null;
        }
    }

}
