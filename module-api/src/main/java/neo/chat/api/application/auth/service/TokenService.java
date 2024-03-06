package neo.chat.api.application.auth.service;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import neo.chat.api.application.auth.model.TokenSet;

public interface TokenService {

    String TOKEN_PREFIX = "Bearer ";
    String ACCESS_TOKEN_SUBJECT = "atk";
    String REFRESH_TOKEN_SUBJECT = "rtk";
    String USER_ID_CLAIM = "uid";

    TokenSet publish(String userId);
    DecodedJWT resolve(String token) throws JWTDecodeException;
    DecodedJWT verify(String token) throws JWTVerificationException;

}
