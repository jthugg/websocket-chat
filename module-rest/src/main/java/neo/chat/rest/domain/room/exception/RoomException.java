package neo.chat.rest.domain.room.exception;

public class RoomException {

    public static class MemberNotFoundException extends RuntimeException {
        public static final String MESSAGE = "사용자를 찾을 수 없습니다.";

        public MemberNotFoundException() {
            super(MESSAGE);
        }
    }

}
