package neo.chat.sync.config.participant;

import neo.chat.persistence.command.entity.CParticipant;
import neo.chat.sync.dto.EventMessageValue;
import neo.chat.sync.util.EventMessageDeserializerSupport;
import org.apache.kafka.common.serialization.Deserializer;

public class ParticipantEventBrokerDeserializer
        extends EventMessageDeserializerSupport
        implements Deserializer<EventMessageValue<CParticipant>> {

    @Override
    public EventMessageValue<CParticipant> deserialize(String topic, byte[] data) {
        return getMessageValue(data, CParticipant.class);
    }

}
