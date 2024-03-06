package neo.chat.api.application.auth.service;

import neo.chat.api.persistence.entity.member.Member;
import neo.chat.api.settings.exception.model.authentication.UsernameDuplicatedException;

public interface MemberAuthService {

    Member register(String username, String password) throws UsernameDuplicatedException;
    Member matchUserData(String username, String password);
    Member getUserDataById(long userId);

}
