package neo.chat.rest.domain.member.service;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.query.MemberQueryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleMemberService implements MemberService {

    private final MemberQueryRepository memberQueryRepository;

    @Override
    public boolean isAvailable(String username) {
        return !memberQueryRepository.existsByUsername(username);
    }

}
