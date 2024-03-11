package neo.chat.integration.auth;

import neo.chat.application.service.auth.service.MemberAuthService;
import neo.chat.application.service.auth.service.SimpleMemberAuthService;
import neo.chat.presentation.auth.controller.MemberAuthController;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Import(SecurityConfig.class)
@WebMvcTest(MemberAuthController.class)
public class MemberAuthControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    MemberAuthService memberAuthService;

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

}