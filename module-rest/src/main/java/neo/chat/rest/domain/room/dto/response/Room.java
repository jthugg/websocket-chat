package neo.chat.rest.domain.room.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import neo.chat.persistence.command.entity.CRoom;
import neo.chat.persistence.query.document.QRoom;

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
        int attending,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Set<Participant> participant,
        Instant createdAt
) {
    public static Room asDetail(CRoom room) {
        return new Room(
                room.getId(),
                room.getTitle(),
                room.getCapacity(),
                room.getPassword() != null,
                Host.from(room.getHost()),
                room.getParticipants().size(),
                room.getParticipants().stream().map(Participant::from).collect(Collectors.toSet()),
                room.getCreatedAt()
        );
    }

    public static Room asSummary(QRoom room) {
        return new Room(
                room.getId(),
                room.getTitle(),
                room.getCapacity(),
                room.getPassword() != null,
                Host.from(room.getHost()),
                room.getParticipants().size(),
                null,
                room.getCreatedAt()
        );
    }
}
