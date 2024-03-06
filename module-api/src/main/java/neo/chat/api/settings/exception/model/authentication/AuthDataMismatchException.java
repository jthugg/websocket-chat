package neo.chat.api.settings.exception.model.authentication;

public class AuthDataMismatchException extends AuthenticationException {

    public static final String MESSAGE = "인증 정보가 일치하지 않습니다.";

    public AuthDataMismatchException() {
        super(MESSAGE);
    }

    public AuthDataMismatchException(Throwable throwable) {
        super(MESSAGE, throwable);
    }

    public AuthDataMismatchException(String message) {
        super(message);
    }

    public AuthDataMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

}
