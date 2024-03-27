package neo.chat.application.service.room.exception;

public class HostNotReplacedException extends RuntimeException {

    public static final String MESSAGE = "다른 참여자를 호스트로 임명해야 합니다.";

    public HostNotReplacedException() {
        super(MESSAGE);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }

}
