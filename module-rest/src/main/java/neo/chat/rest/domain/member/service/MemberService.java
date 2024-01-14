package neo.chat.rest.domain.member.service;

import neo.chat.persistence.command.entity.CMember;
import neo.chat.persistence.query.document.QMember;

public interface MemberService {

    /**
     * 회원가입 전 유저명 중복 검증
     * @param username
     * @return boolean
     */
    boolean isAvailable(String username);

    /**
     * 회원가입
     *
     * @param username
     * @param password
     * @return CMember
     */
    CMember register(String username, String password);

    /**
     * 로그인
     *
     * @param username
     * @param password
     * @return QMember
     */
    QMember login(String username, String password);

}
