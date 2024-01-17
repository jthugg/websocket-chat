package neo.chat.sync.config.participant;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.command.entity.CParticipant;
import neo.chat.sync.dto.EventMessageValue;
import neo.chat.sync.util.EventBrokerProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ParticipantEventBrokerConfig {

    public static final String CONTAINER_FACTORY = "participantContainerFactory";

    private final EventBrokerProperties eventBrokerProperties;

    @Bean
    public ConsumerFactory<String, EventMessageValue<CParticipant>> participantConsumerfactory() {
        return new DefaultKafkaConsumerFactory<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, eventBrokerProperties.getBootstrapServers(),
                ConsumerConfig.GROUP_ID_CONFIG, eventBrokerProperties.getGroupId(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ParticipantEventBrokerDeserializer.class
        ));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventMessageValue<CParticipant>> participantContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EventMessageValue<CParticipant>> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(participantConsumerfactory());
        return factory;
    }
}
