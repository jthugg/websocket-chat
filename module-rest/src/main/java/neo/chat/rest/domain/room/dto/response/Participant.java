package neo.chat.rest.domain.room.dto.response;

import neo.chat.persistence.command.entity.CParticipant;

import java.time.Instant;
import java.util.UUID;

public record Participant(
        UUID id, // participating id
        UUID memberId, // member id
        String username,
        boolean isHost,
        Instant joinAt
) {
    public static Participant from(CParticipant participant) {
        return new Participant(
                participant.getId(),
                participant.getMember().getId(),
                participant.getMember().getUsername(),
                participant.getIsHost(),
                participant.getCreatedAt()
        );
    }
}
