package neo.chat.application.service.room.exception;

public class AlreadyEnteredRoomException extends RuntimeException {

    public static final String MESSAGE = "이미 참여 중인 채팅 방 입니다.";

    public AlreadyEnteredRoomException() {
        super(MESSAGE);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }

}
