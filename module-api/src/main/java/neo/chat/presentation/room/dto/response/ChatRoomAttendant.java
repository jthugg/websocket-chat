package neo.chat.presentation.room.dto.response;

import neo.chat.persistence.entity.participant.Participant;

import java.time.Instant;

public record ChatRoomAttendant(
        Long id,
        Long roomId,
        String nickname,
        boolean isHost,
        Instant joinAt
) {

    public static ChatRoomAttendant from(Participant participant) {
        return new ChatRoomAttendant(
                participant.getId(),
                participant.getRoom().getId(),
                participant.getNickname(),
                participant.getIsHost(),
                participant.getCreatedAt()
        );
    }

}
