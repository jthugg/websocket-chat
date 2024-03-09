package neo.chat.application.service.auth.service;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SimpleMemberAuthService implements MemberAuthService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return !memberRepository.existsByUsername(username);
    }

}
