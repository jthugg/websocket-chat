package neo.chat.rest.domain.member.dto;

public class InputValidation {

    public static final String USERNAME_REGEXP = "^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ]{4,12}$";
    public static final String PASSWORD_REGEXP = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";
    public static final String USERNAME_MESSAGE = "사용자 이름은 한글, 영문, 숫자 조합 4 ~ 12자리 입니다.";
    public static final String PASSWORD_MESSAGE = "비밀번호는 영문, 숫자, 특수문자(!@#$%^*+=-) 조합 8 ~ 15자리 입니다.";

}
