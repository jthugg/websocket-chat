package neo.chat.websocket.controller;

import lombok.RequiredArgsConstructor;
import neo.chat.kafka.config.ChatEventBrokerConfig;
import neo.chat.kafka.model.EventMessageValue;
import neo.chat.kafka.model.OperationType;
import neo.chat.persistence.command.entity.CMessage;
import neo.chat.websocket.service.MessageService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    @MessageMapping("/rooms/{roomId}")
    public void createMessage(@DestinationVariable UUID roomId, @Payload String payload, Principal principal) {
        messageService.create(roomId, UUID.fromString(principal.getName()), payload);
    }

    @KafkaListener(topics = "chat-command.chat.Message", containerFactory = ChatEventBrokerConfig.CONTAINER_FACTORY)
    public void publishMessage(ConsumerRecord<String, EventMessageValue<CMessage>> data) {
        if (OperationType.CREATE == data.value().payload().operationType()) {
            messageService.publish(data.value().payload().after());
        }
    }

}
