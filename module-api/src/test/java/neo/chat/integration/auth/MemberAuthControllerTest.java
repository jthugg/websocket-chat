package neo.chat.integration.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import neo.chat.application.service.auth.exception.InvalidTokenException;
import neo.chat.application.service.auth.exception.MemberNotFoundException;
import neo.chat.application.service.auth.exception.MemberPasswordNotMatchedException;
import neo.chat.application.service.auth.model.AuthResult;
import neo.chat.application.service.auth.properties.JWTProperties;
import neo.chat.application.service.auth.service.MemberAuthService;
import neo.chat.persistence.entity.member.Member;
import neo.chat.presentation.auth.controller.MemberAuthController;
import neo.chat.presentation.auth.dto.request.LoginRequestDto;
import neo.chat.presentation.auth.dto.request.RegisterRequestDto;
import neo.chat.settings.route.ApiRoute;
import neo.chat.settings.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;

@Import(SecurityConfig.class)
@WebMvcTest(MemberAuthController.class)
public class MemberAuthControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    MemberAuthService memberAuthService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 아이디 중복 체크: 성공 케이스")
    void checkUsernameTestCase01() throws Exception {
        Mockito.when(memberAuthService.isUsernameAvailable(ArgumentMatchers.anyString())).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.AUTH_CHECK_USERNAME)
                        .param("value", "my-App_Test.1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    @DisplayName("회원 아이디 중복 체크: 실패 케이스 - 로그인된 사용자의 요청")
    void checkUsernameTestCase02() throws Exception {
        Mockito.when(memberAuthService.isUsernameAvailable(ArgumentMatchers.anyString())).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.AUTH_CHECK_USERNAME).param("value", "test"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 아이디 중복 체크: 실패 케이스 - 이미 사용중인 아이디")
    void checkUsernameTestCase03() throws Exception {
        Mockito.when(memberAuthService.isUsernameAvailable(ArgumentMatchers.anyString())).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.AUTH_CHECK_USERNAME).param("value", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 아이디 중복 체크: 실패 케이스 - 회원 아이디 제약조건 위반: 너무 짧은 문자열")
    void checkUsernameTestCase04() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.AUTH_CHECK_USERNAME).param("value", "t"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 아이디 중복 체크: 실패 케이스 - 회원 아이디 제약조건 위반: 너무 긴 문자열")
    void checkUsernameTestCase05() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.AUTH_CHECK_USERNAME)
                        .param("value", "testtesttesttesttesttest"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 아이디 중복 체크: 실패 케이스 - 회원 아이디 제약조건 위반: 허용되지 않는 문자 조합")
    void checkUsernameTestCase06() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.AUTH_CHECK_USERNAME).param("value", "test!!"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 가입: 성공 케이스")
    void registerTestCase01() throws Exception {
        Mockito.when(memberAuthService.register(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(new AuthResult(
                        new Member(100L, "test", "TestPassword123!"),
                        "testAccessToken",
                        "testRefreshToken",
                        0L,
                        0L
                ));

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(new RegisterRequestDto(
                                "test",
                                "TestPassword123!"
                        ))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 가입: 실패 케이스 - 아이디 제약조건 위반")
    void registerTestCase02() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(new RegisterRequestDto(
                                "test!",
                                "TestPassword123!"
                        ))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 가입: 실패 케이스 - 비밀번호 제약조건 위반")
    void registerTestCase03() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(new RegisterRequestDto(
                                "test",
                                "TestPassword123"
                        ))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 가입: 실패 케이스 - 이미 가입된 아이디")
    void registerTestCase04() throws Exception {
        Mockito.when(memberAuthService.register(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                        .thenThrow(DataIntegrityViolationException.class);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(new RegisterRequestDto(
                                "test",
                                "TestPassword123!"
                        ))))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 로그인: 성공 케이스")
    void loginTestCase01() throws Exception {
        Mockito.when(memberAuthService.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(new AuthResult(
                        new Member(100L, "test", "test"),
                        "testAccessToken",
                        "testRefreshToken",
                        0L,
                        0L
                ));

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(new LoginRequestDto(
                        "test",
                        "TestPassword123!"
                ))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 로그인: 실패 케이스 - 아이디 제약조건 위반")
    void loginTestCase02() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(new RegisterRequestDto(
                                "test!",
                                "TestPassword123!"
                        ))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 로그인: 실패 케이스 - 비밀번호 제약조건 위반")
    void loginTestCase03() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(new RegisterRequestDto(
                                "test",
                                "TestPassword123"
                        ))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 로그인: 실패 케이스 - 가입되지 않은 아이디")
    void loginTestCase04() throws Exception {
        Mockito.when(memberAuthService.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenThrow(MemberNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(new RegisterRequestDto(
                                "test",
                                "TestPassword123!"
                        ))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 로그인: 실패 케이스 - 비밀번호 불일치")
    void loginTestCase05() throws Exception {
        Mockito.when(memberAuthService.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenThrow(MemberPasswordNotMatchedException.class);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(new RegisterRequestDto(
                                "test",
                                "TestPassword123!"
                        ))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰 재발급: 성공 케이스")
    void reissueTestCase01() throws Exception {
        String refreshToken = JWT.create()
                .withExpiresAt(Instant.now())
                .sign(Algorithm.HMAC512("test"));
        Cookie cookie = new MockCookie(JWTProperties.REFRESH_TOKEN, refreshToken);
        AuthResult result = new AuthResult(
                new Member(100L, "test", "test"),
                "testAccessToken",
                "testRefreshToken",
                0L,
                0L
        );

        Mockito.when(memberAuthService.reissue(ArgumentMatchers.anyString())).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_REISSUE).cookie(cookie))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰 재발급: 실패 케이스 - rtk 쿠키 없는 경우")
    void reissueTestCase02() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_REISSUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰 재발급: 실패 케이스 - 토큰 검증 실패: 만료, 알수 없는 문자열, 서명 오류 등")
    void reissueTestCase03() throws Exception {
        Cookie cookie = new MockCookie(JWTProperties.REFRESH_TOKEN, "testRefreshToken");

        Mockito.when(memberAuthService.reissue(ArgumentMatchers.anyString()))
                .thenThrow(InvalidTokenException.class);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_REISSUE).cookie(cookie))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰 재발급: 실패 케이스 - 없는 회원 식별자로 생성된 토큰")
    void reissueTestCase04() throws Exception {
        Cookie cookie = new MockCookie(JWTProperties.REFRESH_TOKEN, "testRefreshToken");

        Mockito.when(memberAuthService.reissue(ArgumentMatchers.anyString()))
                .thenThrow(MemberNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.AUTH_REISSUE).cookie(cookie))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

}
