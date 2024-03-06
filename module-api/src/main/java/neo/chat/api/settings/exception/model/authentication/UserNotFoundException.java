package neo.chat.api.settings.exception.model.authentication;

public class UserNotFoundException extends AuthenticationException {

    public static final String MESSAGE = "사용자를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }

    public UserNotFoundException(Throwable throwable) {
        super(MESSAGE, throwable);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
