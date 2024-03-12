package neo.chat.application.service.auth.exception;

import neo.chat.application.service.exception.ApplicationException;

public class MemberNotFoundException extends ApplicationException {

    public static final String MESSAGE = "회원 정보를 찾을 수 없습니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }

}
