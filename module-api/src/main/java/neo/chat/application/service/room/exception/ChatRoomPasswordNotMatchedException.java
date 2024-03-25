package neo.chat.application.service.room.exception;

public class ChatRoomPasswordNotMatchedException extends RuntimeException {

    public static final String MESSAGE = "채팅 방 비밀번호가 일치하지 않습니다.";

    public ChatRoomPasswordNotMatchedException() {
        super(MESSAGE);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }

}
