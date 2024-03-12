package neo.chat.application.service.auth.exception;

import neo.chat.application.service.exception.ApplicationException;

public class InvalidTokenException extends ApplicationException {

    public static final String MESSAGE = "사용할 수 없는 토큰입니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }

}
