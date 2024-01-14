package neo.chat.sync.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EventBrokerProperties {

    private final String bootstrapServers;
    private final String groupId;

    public EventBrokerProperties(
            @Value("${spring.kafka.consumer.bootstrap-servers}") String bootstrapServers,
            @Value("${spring.kafka.consumer.group-id}") String groupId
    ) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
    }
}
