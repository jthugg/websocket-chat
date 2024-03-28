package neo.chat.presentation.room.dto.response;

import neo.chat.persistence.entity.room.Room;

import java.time.Instant;
import java.util.List;

public record ChatRoomDetails(
        Long id,
        String title,
        boolean isPrivate,
        int capacity,
        int attending,
        int saturation,
        List<ChatRoomAttendant> chatRoomAttendants,
        Instant createdAt
) {

    public static ChatRoomDetails from(Room room) {
        return new ChatRoomDetails(
                room.getId(),
                room.getTitle(),
                room.isPublicRoom(),
                room.getCapacity(),
                room.getAttending(),
                room.getSaturation(),
                room.getParticipants().stream().map(ChatRoomAttendant::from).toList(),
                room.getCreatedAt()
        );
    }

}
