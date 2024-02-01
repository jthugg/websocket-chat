package neo.chat.websocket.service;

import neo.chat.persistence.command.entity.CMessage;

import java.util.UUID;

public interface MessageService {

    void create(UUID roomId, UUID memberId, String payload);
    void publish(CMessage message);

}
