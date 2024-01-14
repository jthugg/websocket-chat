package neo.chat.sync.dto;

import neo.chat.persistence.command.util.JpaEntity;

public record EventMessageValue<T extends JpaEntity>(
        EventPayload<T> payload
) {}
