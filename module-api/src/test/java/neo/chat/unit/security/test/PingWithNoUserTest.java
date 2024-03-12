package neo.chat.unit.security.test;

import neo.chat.settings.route.ApiRoute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("사용자 없이 공개 범위별 엑세스 테스트")
public class PingWithNoUserTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("공개 경로 엑세스 테스트")
    public void pingPublic() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_PERMIT_ALL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("익명 사용자 경로 엑세스 테스트")
    public void pingAnonymous() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_ANONYMOUS))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("인증이 필요한 경로 엑세스 테스트")
    public void pingAuthenticated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_AUTHENTICATED))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

}
