package neo.chat.rest.domain.room.exception;

public class RoomException {

    public static class MemberNotFoundException extends RuntimeException {
        public static final String MESSAGE = "사용자를 찾을 수 없습니다.";

        public MemberNotFoundException() {
            super(MESSAGE);
        }
    }

    public static class RoomNotFoundException extends RuntimeException {
        public static final String MESSAGE = "채팅방을 찾을 수 없습니다.";

        public RoomNotFoundException() {
            super(MESSAGE);
        }
    }

    public static class AlreadyParticipatingRoom extends RuntimeException {
        public static final String MESSAGE = "이미 참여중인 채팅방 입니다.";

        public AlreadyParticipatingRoom() {
            super(MESSAGE);
        }
    }

}
