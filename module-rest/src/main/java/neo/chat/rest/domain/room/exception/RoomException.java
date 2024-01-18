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

    public static class RoomPasswordNotMatchedException extends RuntimeException {
        public static final String MESSAGE = "채팅방 비밀번호가 일치하지 않습니다.";

        public RoomPasswordNotMatchedException() {
            super(MESSAGE);
        }
    }

    public static class AlreadyParticipatingRoomException extends RuntimeException {
        public static final String MESSAGE = "이미 참여중인 채팅방 입니다.";

        public AlreadyParticipatingRoomException() {
            super(MESSAGE);
        }
    }

    public static class NoVacancyInChatRoomException extends RuntimeException {
        public static final String MESSAGE = "채팅방이 가득 찼습니다.";

        public NoVacancyInChatRoomException() {
            super(MESSAGE);
        }
    }

    public static class HostCannotLeaveException extends RuntimeException {
        public static final String MESSAGE = "호스트는 다른 사람이 있는 방을 떠날 수 없습니다. 새로운 호스트를 지정해주세요.";

        public HostCannotLeaveException() {
            super(MESSAGE);
        }
    }

    public static class HostAuthorityRequiredException extends RuntimeException {
        public static final String MESSAGE = "호스트 권한이 필요합니다.";

        public HostAuthorityRequiredException() {
            super(MESSAGE);
        }
    }

    public static class CannotDeleteChatRoomException extends RuntimeException {
        public static final String MESSAGE = "채팅방을 삭제할 수 없습니다.";

        public CannotDeleteChatRoomException() {
            super(MESSAGE);
        }
    }

}
