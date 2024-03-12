package neo.chat.application.service.auth.exception;

import neo.chat.application.service.exception.ApplicationException;

public class MemberPasswordNotMatchedException extends ApplicationException {

    public static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public MemberPasswordNotMatchedException() {
        super(MESSAGE);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }

}
