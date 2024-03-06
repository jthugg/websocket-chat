package neo.chat.api.test.token;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import neo.chat.api.application.auth.service.SimpleTokenService;
import neo.chat.api.application.auth.service.TokenService;
import neo.chat.api.settings.bean.JwtBeanRegister;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest({JwtBeanRegister.class, TokenService.class})
public class TokenServiceTest {

    @Autowired
    Algorithm algorithm;
    @Autowired
    JWTVerifier verifier;
    @Autowired
    TokenService tokenService;

    @Test
    public void publishTest() {
        // Given
        String userId = "0";

        // When & Then
        Assertions.assertDoesNotThrow(() -> {
            String token = tokenService.publish(userId).accessToken();
            DecodedJWT decodedJWT = verifier.verify(token);
            String resolvedUserID = decodedJWT.getClaim(TokenService.USER_ID_CLAIM).asString();
            assert userId.equals(resolvedUserID);
        });
    }

    @Test
    public void tokenVerifyTest() {
        // Given
        String userId = "0";
        long expiredAtkTTL = 0;
        long expiredRtkTTL = 0;
        TokenService expiredTokenService = new SimpleTokenService(algorithm, verifier, expiredAtkTTL, expiredRtkTTL);

        // When
        String availableToken = tokenService.publish(userId).accessToken();
        String expiredToken = expiredTokenService.publish(userId).accessToken();

        // Then
        Assertions.assertDoesNotThrow(() -> tokenService.verify(availableToken));
        Assertions.assertThrows(JWTVerificationException.class, () -> expiredTokenService.verify(expiredToken));
        Assertions.assertDoesNotThrow(() -> expiredTokenService.resolve(expiredToken));
    }

    @Test
    public void tokenResolveTest() {
        // Given
        String userId = "0";
        String validToken = tokenService.publish(userId).accessToken();
        String notValidString = "not valid string";

        // When & Then
        Assertions.assertDoesNotThrow(() -> tokenService.resolve(validToken));
        Assertions.assertThrows(JWTDecodeException.class, () -> tokenService.resolve(notValidString));
    }

}
