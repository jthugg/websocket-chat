package neo.chat.rest.domain.room.dto.response;

import neo.chat.persistence.command.entity.CRoom;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record Room(
        UUID id,
        String title,
        int capacity,
        boolean isPrivate,
        Host host,
        Set<Participant> participant,
        Instant createdAt
) {
    public static Room from(CRoom room) {
        return new Room(
                room.getId(),
                room.getTitle(),
                room.getCapacity(),
                room.getPassword() != null,
                Host.from(room.getHost()),
                room.getParticipants().stream().map(Participant::from).collect(Collectors.toSet()),
                room.getCreatedAt()
        );
    }
}
