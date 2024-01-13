package neo.chat.rest.domain.member.dto;

public class InputValidation {

    public static final String USERNAME_REGEXP = "^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ]{4,12}$";
    public static final String USERNAME_MESSAGE = "사용자 이름은 한글, 영문, 숫자 조합 4 ~ 12자리 입니다.";

}
