package neo.chat.sync.config.message;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.command.entity.CMember;
import neo.chat.persistence.command.entity.CMessage;
import neo.chat.sync.config.member.MemberEventMessageDeserializer;
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
public class MessageEventBrokerConfig {

    public static final String CONTAINER_FACTORY = "messageContainerFactory";

    private final EventBrokerProperties eventBrokerProperties;

    @Bean
    public ConsumerFactory<String, EventMessageValue<CMessage>> messageConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, eventBrokerProperties.getBootstrapServers(),
                ConsumerConfig.GROUP_ID_CONFIG, eventBrokerProperties.getGroupId(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageEventMessageDeserializer.class
        ));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventMessageValue<CMessage>> messageContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EventMessageValue<CMessage>> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messageConsumerFactory());
        return factory;
    }

}
