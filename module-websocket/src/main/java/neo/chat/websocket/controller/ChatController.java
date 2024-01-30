package neo.chat.websocket.controller;

import lombok.RequiredArgsConstructor;
import neo.chat.websocket.service.MessageService;
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

}
