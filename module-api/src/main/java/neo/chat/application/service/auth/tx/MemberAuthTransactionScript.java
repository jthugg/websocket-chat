package neo.chat.application.service.auth.tx;

import lombok.RequiredArgsConstructor;
import neo.chat.application.util.EntityIdGenerator;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.repository.member.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberAuthTransactionScript {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public Member createMember(String username, String password) {
        return memberRepository.save(new Member(
                EntityIdGenerator.MEMBER.getIdGenerator().generate().toLong(),
                username,
                passwordEncoder.encode(password)
        ));
    }

}