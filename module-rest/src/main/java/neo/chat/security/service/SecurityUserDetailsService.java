package neo.chat.security.service;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.query.MemberQueryRepository;
import neo.chat.security.model.SecurityUserDetails;
import neo.chat.security.util.SecurityUserContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    public static final String USER_NOT_FOUND_MESSAGE = "사용자를 찾을 수 없습니다.";

    private final MemberQueryRepository memberQueryRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new SecurityUserDetails(SecurityUserContextHolder.set(memberQueryRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE))));
    }

}
