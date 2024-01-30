package neo.chat.websocket.service;

import java.util.UUID;

public interface MessageService {

    void create(UUID roomId, UUID memberId, String payload);

}
