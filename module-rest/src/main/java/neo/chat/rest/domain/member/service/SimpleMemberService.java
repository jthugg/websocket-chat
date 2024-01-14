package neo.chat.rest.domain.member.service;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.command.MemberCommandRepository;
import neo.chat.persistence.command.config.CommandDBConfig;
import neo.chat.persistence.command.entity.CMember;
import neo.chat.persistence.query.MemberQueryRepository;
import neo.chat.persistence.query.config.QueryDBConfig;
import neo.chat.persistence.query.document.QMember;
import neo.chat.rest.domain.member.exception.MemberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SimpleMemberService implements MemberService {

    private final MemberQueryRepository memberQueryRepository;
    private final MemberCommandRepository memberCommandRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true, transactionManager = QueryDBConfig.TRANSACTION_MANAGER)
    public boolean isAvailable(String username) {
        return !memberQueryRepository.existsByUsername(username);
    }

    @Override
    @Transactional(transactionManager = CommandDBConfig.TRANSACTION_MANAGER)
    public CMember register(String username, String password) {
        return memberCommandRepository.save(CMember.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build());
    }

    @Override
    @Transactional(transactionManager = QueryDBConfig.TRANSACTION_MANAGER)
    public QMember login(String username, String password) {
        QMember member = memberQueryRepository.findByUsername(username)
                .orElseThrow(MemberException.LoginUsernameNotFoundException::new);
        if (passwordEncoder.matches(password, member.getPassword())) {
            return member;
        }
        throw new MemberException.PasswordNotMatchedException();
    }

}
