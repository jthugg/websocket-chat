package neo.chat.websocket.model;

import java.time.Instant;
import java.util.UUID;

public record ChatMessage(
        UUID roomId,
        UUID sender,
        String content,
        Instant createdAt
) {}
