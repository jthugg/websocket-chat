package neo.chat.jwt.util;

public class InvalidTokenException extends RuntimeException {

    public static final String MESSAGE = "사용할 수 없는 토큰입니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }

}
