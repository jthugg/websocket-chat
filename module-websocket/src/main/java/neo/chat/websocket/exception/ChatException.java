package neo.chat.websocket.exception;

public class ChatException {

    public static class InvalidUsernameException extends RuntimeException {
        public static final String MESSAGE = "사용자를 찾을 수 없습니다.";

        public InvalidUsernameException() {
            super(MESSAGE);
        }
    }

    public static class InvalidAccessException extends RuntimeException {
        public static final String MESSAGE = "비정상 접근입니다.";

        public InvalidAccessException() {
            super(MESSAGE);
        }
    }

}
