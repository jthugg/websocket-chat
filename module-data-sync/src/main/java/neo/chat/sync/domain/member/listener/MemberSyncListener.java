package neo.chat.sync.domain.member.listener;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.command.entity.CMember;
import neo.chat.sync.config.member.MemberEventBrokerConfig;
import neo.chat.sync.domain.member.service.MemberSyncService;
import neo.chat.sync.dto.EventMessageValue;
import neo.chat.sync.dto.EventPayload;
import neo.chat.sync.util.Topic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MemberSyncListener {

    private final MemberSyncService memberSyncService;

    @KafkaListener(topics = Topic.MEMBER, containerFactory = MemberEventBrokerConfig.CONTAINER_FACTORY)
    public void consume(ConsumerRecord<String, EventMessageValue<CMember>> data) {
        if (data.value() != null) {
            route(data.value().payload());
        }
    }

    private void route(EventPayload<CMember> payload) {
        switch (payload.operationType()) {
            case CREATE -> memberSyncService.create(payload.after());
            case UPDATE -> memberSyncService.update(payload.before(), payload.after());
            case DELETE -> memberSyncService.delete(payload.before());
        }
    }

}
