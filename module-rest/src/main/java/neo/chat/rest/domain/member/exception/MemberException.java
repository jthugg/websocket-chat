package neo.chat.rest.domain.member.exception;

public class MemberException {

    public static class LoginUsernameNotFoundException extends RuntimeException {
        public static final String MESSAGE = "사용자를 찾을 수 없습니다.";

        public LoginUsernameNotFoundException() {
            super(MESSAGE);
        }
    }

    public static class PasswordNotMatchedException extends RuntimeException {
        public static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

        public PasswordNotMatchedException() {
            super(MESSAGE);
        }
    }

    public static class UsernameNotFoundException extends RuntimeException {
        public static final String MESSAGE = "사용자를 찾을 수 없습니다.";

        public UsernameNotFoundException() {
            super(MESSAGE);
        }
    }

}
