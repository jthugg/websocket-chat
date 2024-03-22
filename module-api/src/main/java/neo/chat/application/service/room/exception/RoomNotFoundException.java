package neo.chat.application.service.room.exception;

import neo.chat.application.service.exception.ApplicationException;

public class RoomNotFoundException extends ApplicationException {

    public static final String MESSAGE = "채팅 방을 찾을 수 없습니다.";

    public RoomNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }

}
