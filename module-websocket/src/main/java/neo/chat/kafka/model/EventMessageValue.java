package neo.chat.kafka.model;

import neo.chat.persistence.command.util.JpaEntity;

public record EventMessageValue<T extends JpaEntity>(
        EventPayload<T> payload
) {}
