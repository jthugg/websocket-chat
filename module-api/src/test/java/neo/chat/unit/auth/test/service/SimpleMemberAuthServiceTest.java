package neo.chat.unit.auth.test.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.JWTVerifier;
import neo.chat.application.service.auth.exception.InvalidTokenException;
import neo.chat.application.service.auth.exception.MemberNotFoundException;
import neo.chat.application.service.auth.exception.MemberPasswordNotMatchedException;
import neo.chat.application.service.auth.model.AuthResult;
import neo.chat.application.service.auth.properties.JWTProperties;
import neo.chat.application.service.auth.service.SimpleMemberAuthService;
import neo.chat.application.service.auth.tx.MemberAuthTransactionScript;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.repository.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
public class SimpleMemberAuthServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    MemberAuthTransactionScript memberAuthTransactionScript;
    @Mock
    JWTProperties jwtProperties;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JWTVerifier jwtVerifier;
    @InjectMocks
    SimpleMemberAuthService simpleMemberAuthService;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("회원 아이디 중복 체크: value={데이터베이스 조회 결과}")
    void isUsernameAvailableTest(boolean value) {
        String username = "username";
        Mockito.when(memberRepository.existsByUsername(ArgumentMatchers.any())).thenReturn(value);
        Assertions.assertNotEquals(
                simpleMemberAuthService.isUsernameAvailable(username),
                memberRepository.existsByUsername(username)
        );
    }

    @Test
    @DisplayName("회원 가입: 성공 케이스")
    void registerTest() {
        String username = "test";
        String password = "test";
        Member member = new Member(0L, username, password);

        Mockito.when(memberAuthTransactionScript.createMember(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString())
                ).thenReturn(member);
        Mockito.when(jwtProperties.algorithm()).thenReturn(Algorithm.HMAC512("test"));
        Mockito.when(jwtProperties.atkTTL()).thenReturn(0L);
        Mockito.when(jwtProperties.rtkTTL()).thenReturn(0L);

        Assertions.assertDoesNotThrow(() -> {
            AuthResult result = simpleMemberAuthService.register(username, password);
            Assertions.assertEquals(result.member(), member);
            Assertions.assertEquals(
                    JWT.decode(result.accessToken()).getClaim(JWTProperties.USER_ID).asLong(),
                    member.getId()
            );
            Assertions.assertEquals(
                    JWT.decode(result.refreshToken()).getClaim(JWTProperties.USER_ID).asLong(),
                    member.getId()
            );
            JWTVerifier verifier = JWT.require(jwtProperties.algorithm()).build();
            Assertions.assertThrows(TokenExpiredException.class, () -> verifier.verify(result.accessToken()));
            Assertions.assertThrows(TokenExpiredException.class, () -> verifier.verify(result.refreshToken()));
        });
    }

    @Test
    @DisplayName("로그인: 성공 케이스")
    void loginTestCase01() {
        String username = "test";
        String password = "test";

        Mockito.when(memberAuthTransactionScript.readMemberByUsername(ArgumentMatchers.anyString()))
                .thenReturn(new Member(100L, username, password));
        Mockito.when(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(true);
        Mockito.when(jwtProperties.algorithm()).thenReturn(Algorithm.HMAC512("test"));

        Assertions.assertDoesNotThrow(() -> simpleMemberAuthService.login(username, password));
    }

    @Test
    @DisplayName("로그인: 실패 케이스 - 사용자를 찾을 수 없을 때")
    void loginTestCase02() {
        String username = "test";
        String password = "test";

        Mockito.when(memberAuthTransactionScript.readMemberByUsername(ArgumentMatchers.anyString()))
                .thenThrow(MemberNotFoundException.class);

        Assertions.assertThrows(MemberNotFoundException.class, () -> simpleMemberAuthService.login(username, password));
    }

    @Test
    @DisplayName("로그인: 실패 케이스 - 비밀번호가 일치하지 않을 때")
    void loginTestCase03() {
        String username = "test";
        String password = "test";

        Mockito.when(memberAuthTransactionScript.readMemberByUsername(ArgumentMatchers.anyString()))
                .thenReturn(new Member(100L, username, password));
        Mockito.when(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(false);

        Assertions.assertThrows(
                MemberPasswordNotMatchedException.class,
                () -> simpleMemberAuthService.login(username, password)
        );
    }

    @Test
    @DisplayName("토큰 재발급: 성공 케이스")
    void reissueTestCase01() {
        String testSecret = "testSecret";
        Algorithm algorithm = Algorithm.HMAC512(testSecret);
        long atkTTL = 300;
        long rtkTTL = 300;
        Long memberId = 100L;
        String token = JWT.create()
                .withClaim(JWTProperties.USER_ID, memberId)
                .withClaim(JWTProperties.TYPE, JWTProperties.REFRESH_TOKEN)
                .withExpiresAt(Instant.now().plus(rtkTTL, ChronoUnit.SECONDS))
                .sign(algorithm);
        Member member = new Member(memberId, "test", "test");

        Mockito.when(jwtVerifier.verify(token)).thenReturn(JWT.decode(token));
        Mockito.when(memberAuthTransactionScript.readMemberById(memberId)).thenReturn(member);
        Mockito.when(jwtProperties.algorithm()).thenReturn(algorithm);
        Mockito.when(jwtProperties.atkTTL()).thenReturn(atkTTL);
        Mockito.when(jwtProperties.rtkTTL()).thenReturn(rtkTTL);

        Assertions.assertDoesNotThrow(() -> simpleMemberAuthService.reissue(token));
    }

    @Test
    @DisplayName("토큰 재발급: 실패 케이스 - 만료된 토큰")
    void reissueTestCase02() {
        String testTokenValue = "test";

        Mockito.when(jwtVerifier.verify(testTokenValue)).thenThrow(TokenExpiredException.class);

        Assertions.assertThrows(InvalidTokenException.class, () -> simpleMemberAuthService.reissue(testTokenValue));
    }

    @Test
    @DisplayName("토큰 재발급: 실패 케이스 - 토큰 서명 오류 1")
    void reissueTestCase03() {
        String testTokenValue = "test";

        Mockito.when(jwtVerifier.verify(testTokenValue)).thenThrow(AlgorithmMismatchException.class);

        Assertions.assertThrows(InvalidTokenException.class, () -> simpleMemberAuthService.reissue(testTokenValue));
    }

    @Test
    @DisplayName("토큰 재발급: 실패 케이스 - 토큰 서명 오류 2")
    void reissueTestCase04() {
        String testTokenValue = "test";

        Mockito.when(jwtVerifier.verify(testTokenValue)).thenThrow(SignatureVerificationException.class);

        Assertions.assertThrows(InvalidTokenException.class, () -> simpleMemberAuthService.reissue(testTokenValue));
    }

    @Test
    @DisplayName("토큰 재발급: 성공 케이스 - 알 수 없는 문자열 토큰 입력")
    void reissueTestCase05() {
        String testTokenValue = "test";

        Mockito.when(jwtVerifier.verify(testTokenValue)).thenThrow(JWTDecodeException.class);

        Assertions.assertThrows(InvalidTokenException.class, () -> simpleMemberAuthService.reissue(testTokenValue));
    }

    @Test
    @DisplayName("토큰 재발급: 실패 케이스 - 없는 회원 조회 요청")
    void reissueTestCase06() {
        String testSecret = "testSecret";
        Algorithm algorithm = Algorithm.HMAC512(testSecret);
        long rtkTTL = 300;
        Long memberId = 100L;
        String token = JWT.create()
                .withClaim(JWTProperties.USER_ID, memberId)
                .withClaim(JWTProperties.TYPE, JWTProperties.REFRESH_TOKEN)
                .withExpiresAt(Instant.now().plus(rtkTTL, ChronoUnit.SECONDS))
                .sign(algorithm);

        Mockito.when(jwtVerifier.verify(token)).thenReturn(JWT.decode(token));
        Mockito.when(memberAuthTransactionScript.readMemberById(memberId)).thenThrow(MemberNotFoundException.class);

        Assertions.assertThrows(MemberNotFoundException.class, () -> simpleMemberAuthService.reissue(token));
    }

}
