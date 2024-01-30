package neo.chat.sync.config.message;

import neo.chat.persistence.command.entity.CMessage;
import neo.chat.sync.dto.EventMessageValue;
import neo.chat.sync.util.EventMessageDeserializerSupport;
import org.apache.kafka.common.serialization.Deserializer;

public class MessageEventMessageDeserializer
        extends EventMessageDeserializerSupport
        implements Deserializer<EventMessageValue<CMessage>> {

    @Override
    public EventMessageValue<CMessage> deserialize(String topic, byte[] data) {
        return getMessageValue(data, CMessage.class);
    }

}
