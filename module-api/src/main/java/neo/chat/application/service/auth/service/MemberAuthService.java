package neo.chat.application.service.auth.service;

import neo.chat.application.service.auth.model.AuthResult;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberAuthService extends UserDetailsService {

    boolean isUsernameAvailable(String username);
    AuthResult register(String username, String password);
    AuthResult login(String username, String password);
    AuthResult reissue(String refreshToken);
    UserDetails authorization(String accessToken);
    void withdraw(String password);

}
