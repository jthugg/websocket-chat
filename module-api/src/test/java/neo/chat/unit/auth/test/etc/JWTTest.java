package neo.chat.unit.auth.test.etc;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import neo.chat.application.service.auth.properties.JWTProperties;
import neo.chat.persistence.entity.member.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@DisplayName("JWT 토큰 테스트")
public class JWTTest {

    Algorithm algorithm = Algorithm.HMAC512("testSecretKey");
    Member member = new Member(100L, "test", "test");
    JWTVerifier jwtVerifier = JWT.require(algorithm).build();

    @Test
    @DisplayName("토큰 생성 테스트")
    void createTest() {
        log.info("\n\n\ntoken value: {}\n\n", JWT.create()
                .withClaim(JWTProperties.USER_ID, member.getId())
                .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .withExpiresAt(Instant.now().plus(300, ChronoUnit.SECONDS))
                .sign(algorithm));
    }

    @Test
    @DisplayName("토큰 검증 테스트")
    void verifyTest() {
        String nonExpiredToken = JWT.create()
                .withClaim(JWTProperties.USER_ID, member.getId())
                .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .withExpiresAt(Instant.now().plus(300, ChronoUnit.SECONDS))
                .sign(algorithm);
        String expiredToken = JWT.create()
                .withClaim(JWTProperties.USER_ID, member.getId())
                .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .withExpiresAt(Instant.now().plus(0, ChronoUnit.SECONDS))
                .sign(algorithm);

        Assertions.assertDoesNotThrow(() -> jwtVerifier.verify(nonExpiredToken));
        Assertions.assertThrows(TokenExpiredException.class, () -> jwtVerifier.verify(expiredToken));
    }

    @Test
    @DisplayName("토큰 디코딩 테스트")
    void decodeTest() {
        String invalidToken = "testString";
        String availableToken = JWT.create()
                .withClaim(JWTProperties.USER_ID, member.getId())
                .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .withExpiresAt(Instant.now().plus(300, ChronoUnit.SECONDS))
                .sign(algorithm);
        String expiredToken = JWT.create()
                .withClaim(JWTProperties.USER_ID, member.getId())
                .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .withExpiresAt(Instant.now().plus(0, ChronoUnit.SECONDS))
                .sign(algorithm);
        String invalidAlgorithmToken01 = JWT.create()
                .withClaim(JWTProperties.USER_ID, member.getId())
                .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .withExpiresAt(Instant.now().plus(0, ChronoUnit.SECONDS))
                .sign(Algorithm.HMAC512("anotherTestSecret"));
        String invalidAlgorithmToken02 = JWT.create()
                .withClaim(JWTProperties.USER_ID, member.getId())
                .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .withExpiresAt(Instant.now().plus(0, ChronoUnit.SECONDS))
                .sign(Algorithm.HMAC256("anotherTestSecret"));

        Assertions.assertThrows(JWTVerificationException.class, () -> jwtVerifier.verify(invalidToken));
        Assertions.assertDoesNotThrow(() -> jwtVerifier.verify(availableToken));
        Assertions.assertThrows(JWTVerificationException.class, () -> jwtVerifier.verify(expiredToken));
        Assertions.assertThrows(JWTVerificationException.class, () -> jwtVerifier.verify(invalidAlgorithmToken01));
        Assertions.assertThrows(JWTVerificationException.class, () -> jwtVerifier.verify(invalidAlgorithmToken02));
    }

}
