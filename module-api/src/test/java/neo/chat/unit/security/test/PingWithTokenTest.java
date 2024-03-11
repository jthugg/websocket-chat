package neo.chat.unit.security.test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import neo.chat.application.service.auth.properties.JWTProperties;
import neo.chat.settings.route.ApiRoute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PingWithTokenTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("공개 경로 토큰 인증 테스트: 성공 케이스")
    void pingPublicTest(@Value("${token.secret}") String secret) throws Exception {
        long memberId = 0L;
        String testTokenValue = JWTProperties.PREFIX + JWT.create()
                .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .withClaim(JWTProperties.USER_ID,  memberId)
                .withExpiresAt(Instant.now().plus(300, ChronoUnit.SECONDS))
                .sign(Algorithm.HMAC512(secret));

        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_PERMIT_ALL)
                        .header(HttpHeaders.AUTHORIZATION, testTokenValue))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("익명 경로 토큰 인증 테스트: 성공 케이스")
    void pingAnonymousTest(@Value("${token.secret}") String secret) throws Exception {
        long memberId = 0L;
        String testTokenValue = JWTProperties.PREFIX + JWT.create()
                .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .withClaim(JWTProperties.USER_ID,  memberId)
                .withExpiresAt(Instant.now().plus(300, ChronoUnit.SECONDS))
                .sign(Algorithm.HMAC512(secret));

        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_ANONYMOUS)
                        .header(HttpHeaders.AUTHORIZATION, testTokenValue))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("인증이 필요한 경로 토큰 인증 테스트: 성공 케이스")
    void pingAuthenticatedTestCase01(@Value("${token.secret}") String secret) throws Exception {
        long memberId = 0L;
        String testTokenValue = JWTProperties.PREFIX + JWT.create()
                .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .withClaim(JWTProperties.USER_ID,  memberId)
                .withExpiresAt(Instant.now().plus(300, ChronoUnit.SECONDS))
                .sign(Algorithm.HMAC512(secret));

        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_AUTHENTICATED)
                        .header(HttpHeaders.AUTHORIZATION, testTokenValue))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("인증이 필요한 경로 토큰 인증 테스트: 실패 케이스 - 토큰이 없는 경우")
    void pingAuthenticatedTestCase02() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_AUTHENTICATED))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("인증이 필요한 경로 토큰 인증 테스트: 실패 케이스 - 토큰 접두어가 없는 경우")
    void pingAuthenticatedTestCase03(@Value("${token.secret}") String secret) throws Exception {
        long memberId = 0L;
        String testTokenValue = JWT.create()
                .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .withClaim(JWTProperties.USER_ID,  memberId)
                .withExpiresAt(Instant.now().plus(300, ChronoUnit.SECONDS))
                .sign(Algorithm.HMAC512(secret));

        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_AUTHENTICATED)
                        .header(HttpHeaders.AUTHORIZATION, testTokenValue))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("인증이 필요한 경로 토큰 인증 테스트: 실패 케이스 - 잘못된 토큰인 경우")
    void pingAuthenticatedTestCase04() throws Exception {
        String testTokenValue = "testTokenValue";

        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_AUTHENTICATED)
                        .header(HttpHeaders.AUTHORIZATION, testTokenValue))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("인증이 필요한 경로 토큰 인증 테스트: 실패 케이스 - 만료된 토큰인 경우")
    void pingAuthenticatedTestCase05(@Value("${token.secret}") String secret) throws Exception {
        long memberId = 0L;
        String testTokenValue = JWTProperties.PREFIX +  JWT.create()
                .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .withClaim(JWTProperties.USER_ID,  memberId)
                .withExpiresAt(Instant.now().plus(0L, ChronoUnit.SECONDS))
                .sign(Algorithm.HMAC512(secret));

        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_AUTHENTICATED)
                        .header(HttpHeaders.AUTHORIZATION, testTokenValue))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

}
