package neo.chat.api.settings.exception.model.authentication;

public class UsernameDuplicatedException extends AuthenticationException {

    public static final String MESSAGE = "이미 사용중인 아이디 입니다.";

    public UsernameDuplicatedException() {
        super(MESSAGE);
    }

    public UsernameDuplicatedException(Throwable throwable) {
        super(MESSAGE, throwable);
    }

    public UsernameDuplicatedException(String message) {
        super(message);
    }

    public UsernameDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }

}
