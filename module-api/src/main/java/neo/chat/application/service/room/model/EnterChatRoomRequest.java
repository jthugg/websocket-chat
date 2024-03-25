package neo.chat.application.service.room.model;

public record EnterChatRoomRequest(
        Long roomId,
        String nickname,
        String password
) {}
