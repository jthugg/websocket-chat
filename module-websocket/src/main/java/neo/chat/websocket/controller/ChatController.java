package neo.chat.websocket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.chat.websocket.service.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    @MessageMapping("/rooms/{roomId}")
    public void createMessage(@DestinationVariable UUID roomId, @Payload String payload, StompHeaderAccessor accessor) {
        assert accessor.getUser() != null;
        messageService.create(roomId, UUID.fromString(accessor.getUser().getName()), payload);
    }

}
