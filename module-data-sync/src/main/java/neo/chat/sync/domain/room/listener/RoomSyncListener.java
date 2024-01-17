package neo.chat.sync.domain.room.listener;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.command.entity.CRoom;
import neo.chat.sync.config.room.RoomEventBrokerConfig;
import neo.chat.sync.domain.room.service.RoomSyncService;
import neo.chat.sync.dto.EventMessageValue;
import neo.chat.sync.dto.EventPayload;
import neo.chat.sync.util.Topic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RoomSyncListener {

    private final RoomSyncService roomSyncService;

    @KafkaListener(topics = Topic.ROOM, containerFactory = RoomEventBrokerConfig.CONTAINER_FACTORY)
    public void consume(ConsumerRecord<String, EventMessageValue<CRoom>> data) {
        if (data != null) {
            route(data.value().payload());
        }
    }

    private void route(EventPayload<CRoom> payload) {
        switch (payload.operationType()) {
            case CREATE -> roomSyncService.create(payload.after().getId(), payload.after());
            case UPDATE -> roomSyncService.update(payload.before().getId(), payload.before(), payload.after());
            case DELETE -> roomSyncService.delete(payload.before().getId());
        }
    }

}
