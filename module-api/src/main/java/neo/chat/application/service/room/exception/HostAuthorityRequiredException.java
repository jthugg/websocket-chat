package neo.chat.application.service.room.exception;

public class HostAuthorityRequiredException extends RuntimeException {

    public static final String MESSAGE = "작업을 수행하려면 채팅 방의 호스트 권한이 필요합니다.";

    public HostAuthorityRequiredException() {
        super(MESSAGE);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }

}
