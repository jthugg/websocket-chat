package neo.chat.application.service.auth.service;

import neo.chat.application.service.auth.model.AuthResult;

public interface MemberAuthService {

    boolean isUsernameAvailable(String username);
    AuthResult register(String username, String password);

}
