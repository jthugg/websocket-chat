package neo.chat.unit.auth.test.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import neo.chat.application.service.auth.model.AuthResult;
import neo.chat.application.service.auth.properties.JWTProperties;
import neo.chat.application.service.auth.service.SimpleMemberAuthService;
import neo.chat.application.service.auth.tx.MemberAuthTransactionScript;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.repository.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SimpleMemberAuthServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    MemberAuthTransactionScript memberAuthTransactionScript;
    @Mock
    JWTProperties jwtProperties;
    @InjectMocks
    SimpleMemberAuthService simpleMemberAuthService;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void isUsernameAvailableTest(boolean value) {
        String username = "username";
        Mockito.when(memberRepository.existsByUsername(ArgumentMatchers.any())).thenReturn(value);
        Assertions.assertNotEquals(
                simpleMemberAuthService.isUsernameAvailable(username),
                memberRepository.existsByUsername(username)
        );
    }

    @Test
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

}
