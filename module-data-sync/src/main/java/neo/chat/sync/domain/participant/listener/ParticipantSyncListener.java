package neo.chat.sync.domain.participant.listener;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.command.entity.CParticipant;
import neo.chat.sync.config.participant.ParticipantEventBrokerConfig;
import neo.chat.sync.domain.participant.service.ParticipantSyncService;
import neo.chat.sync.dto.EventMessageValue;
import neo.chat.sync.dto.EventPayload;
import neo.chat.sync.util.Topic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ParticipantSyncListener {

    private final ParticipantSyncService participantSyncService;

    @KafkaListener(topics = Topic.PARTICIPANT, containerFactory = ParticipantEventBrokerConfig.CONTAINER_FACTORY)
    public void consume(ConsumerRecord<String, EventMessageValue<CParticipant>> data) {
        if (data.value() != null) {
            route(data.value().payload());
        }
    }

    private void route(EventPayload<CParticipant> payload) {
        switch (payload.operationType()) {
            case CREATE -> participantSyncService.create(payload.after().getRoom().getId(), payload.after());
            case UPDATE -> participantSyncService.update(
                    payload.before().getRoom().getId(),
                    payload.before(),
                    payload.after()
            );
            case DELETE -> participantSyncService.delete(payload.before().getRoom().getId(), payload.before());
        }
    }

}
