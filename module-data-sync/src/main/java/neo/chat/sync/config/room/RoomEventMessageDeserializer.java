package neo.chat.sync.config.room;

import neo.chat.persistence.command.entity.CRoom;
import neo.chat.sync.dto.EventMessageValue;
import neo.chat.sync.util.EventMessageDeserializerSupport;
import org.apache.kafka.common.serialization.Deserializer;

public class RoomEventMessageDeserializer
        extends EventMessageDeserializerSupport
        implements Deserializer<EventMessageValue<CRoom>> {

    @Override
    public EventMessageValue<CRoom> deserialize(String topic, byte[] data) {
        return getMessageValue(data, CRoom.class);
    }

}
