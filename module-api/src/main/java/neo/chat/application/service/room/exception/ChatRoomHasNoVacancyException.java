package neo.chat.application.service.room.exception;

public class ChatRoomHasNoVacancyException extends RuntimeException {

    public static final String MESSAGE = "채팅 방이 꽉 찼습니다.";

    public ChatRoomHasNoVacancyException() {
        super(MESSAGE);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }

}
