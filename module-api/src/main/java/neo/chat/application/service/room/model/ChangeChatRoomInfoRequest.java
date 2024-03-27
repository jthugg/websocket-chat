package neo.chat.application.service.room.model;

public record ChangeChatRoomInfoRequest(
        Long id,
        String title,
        String password,
        Integer capacity
) {}
