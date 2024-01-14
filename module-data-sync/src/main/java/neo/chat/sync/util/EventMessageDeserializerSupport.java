package neo.chat.sync.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import neo.chat.persistence.command.util.JpaEntity;
import neo.chat.sync.dto.EventMessageValue;

import java.io.IOException;

@Slf4j
public class EventMessageDeserializerSupport {

    protected static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    protected static final TypeFactory typeFactory = objectMapper.getTypeFactory();

    protected <T extends JpaEntity> EventMessageValue<T> getMessageValue(byte[] data, Class<T> targetType) {
        try {
            return objectMapper.readValue(data, getEventMessageValueType(targetType));
        } catch (IOException e) {
            log.error("문제가 발생했습니다. {}", e.getMessage());
            return null;
        }
    }

    private <T> JavaType getEventMessageValueType(Class<T> tClass) {
        return typeFactory.constructParametricType(EventMessageValue.class, tClass);
    }

}
