package neo.chat.application.service.room.model;

public record OpenChatRoomRequest(
        String title,
        String password,
        int capacity,
        String nickname
) {

    public boolean hasPassword() {
        return password != null;
    }

}
