package neo.chat.sync.domain.message.listener;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.command.entity.CMember;
import neo.chat.persistence.command.entity.CMessage;
import neo.chat.sync.config.message.MessageEventBrokerConfig;
import neo.chat.sync.domain.message.service.MessageSyncService;
import neo.chat.sync.dto.EventMessageValue;
import neo.chat.sync.dto.EventPayload;
import neo.chat.sync.util.Topic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageSyncListener {

    private final MessageSyncService messageSyncService;

    @KafkaListener(topics = Topic.MESSAGE, containerFactory = MessageEventBrokerConfig.CONTAINER_FACTORY)
    public void consume(ConsumerRecord<String, EventMessageValue<CMessage>> data) {
        if (data.value() != null) {
            route(data.value().payload());
        }
    }

    private void route(EventPayload<CMessage> payload) {
        switch (payload.operationType()) {
            case CREATE -> messageSyncService.create(payload.after());
            case UPDATE -> messageSyncService.update(payload.before(), payload.after());
            case DELETE -> messageSyncService.delete(payload.before());
        }
    }

}
