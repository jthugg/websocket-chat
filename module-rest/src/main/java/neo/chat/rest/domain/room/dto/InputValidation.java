package neo.chat.rest.domain.room.dto;

public class InputValidation {

    public static final String TITLE_REGEXP = "^[^\\x80-\\xFF]{1,100}$";
    public static final String PASSWORD_REGEXP = "^[a-zA-Z0-9]{1,20}$";
    public static final int MINIMUM_CAPACITY = 2;
    public static final int MAXIMUM_CAPACITY = 100;
    public static final String TITLE_MESSAGE = "제목은 아스키코드로 나타낼 수 있는 문자 1 ~ 100자리 입니다.";
    public static final String PASSWORD_MESSAGE = "비밀번호는 영어 대/소문자, 숫자 중 한가지 이상을 포함하고 1 ~ 20자리 입니다.";
    public static final String CAPACITY_MESSAGE = "채팅 참여 인원은 2 ~ 100명 입니다.";

}
