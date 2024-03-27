package neo.chat.application.service.room.exception;

public class ParticipantNotFountException extends RuntimeException {

    public static final String MESSAGE = "채팅방 참여자를 찾을 수 없습니다.";

    public ParticipantNotFountException() {
        super(MESSAGE);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }

}
