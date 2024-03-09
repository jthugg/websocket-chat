package neo.chat.application.service.auth.exception;

public class MemberPasswordNotMatchedException extends RuntimeException {

    public static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public MemberPasswordNotMatchedException() {
        super(MESSAGE);
    }

}
