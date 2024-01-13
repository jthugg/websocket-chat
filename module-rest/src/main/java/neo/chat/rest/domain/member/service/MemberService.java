package neo.chat.rest.domain.member.service;

public interface MemberService {

    /**
     * 회원가입 전 유저명 중복 검증
     * @param username
     * @return boolean
     */
    boolean isAvailable(String username);

}
