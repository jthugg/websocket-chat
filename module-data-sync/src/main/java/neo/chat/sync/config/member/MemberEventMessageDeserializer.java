package neo.chat.sync.config.member;

import neo.chat.persistence.command.entity.CMember;
import neo.chat.sync.dto.EventMessageValue;
import neo.chat.sync.util.EventMessageDeserializerSupport;
import org.apache.kafka.common.serialization.Deserializer;

public class MemberEventMessageDeserializer
        extends EventMessageDeserializerSupport
        implements Deserializer<EventMessageValue<CMember>> {

    @Override
    public EventMessageValue<CMember> deserialize(String topic, byte[] data) {
        return getMessageValue(data, CMember.class);
    }

}
