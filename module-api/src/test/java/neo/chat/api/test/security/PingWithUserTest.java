package neo.chat.api.test.security;

import neo.chat.api.settings.route.ApiRoute;
import neo.chat.settings.AbstractMockMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
public class PingWithUserTest extends AbstractMockMvcTest {

    @Test
    @WithMockUser
    public void pingPermitAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_PERMIT_ALL))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void pingAnonymous() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_ANONYMOUS))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser
    public void pingAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiRoute.PING_AUTHENTICATED))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
