package neo.chat.jwt.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import neo.chat.jwt.model.TokenSet;
import neo.chat.jwt.util.InvalidTokenException;

public interface TokenService {

    /**
     * 토큰 생성
     *
     * @param username
     * @return TokenSet
     */
    TokenSet generateAccessToken(String username);
    /**
     * 토큰이 만료됐는지, 비정상토큰인지 검사
     *
     * @param token
     * @return boolean
     * @throws JWTVerificationException any step of JWT verifying except expired
     */
    boolean isExpired(String token) throws InvalidTokenException;
    /**
     * 엑세스 토큰에서 사용자 이름 추출
     *
     * @param accessToken
     * @return String
     * @throws JWTVerificationException any step of JWT verifying except expired
     */
    String getUsername(String accessToken) throws InvalidTokenException;
    /**
     * 토큰 블랙리스팅
     *
     * @param tokens
     */
    void blacklist(String... tokens);

}
