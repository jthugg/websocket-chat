package neo.chat.api.application.auth.service;

import lombok.RequiredArgsConstructor;
import neo.chat.api.persistence.entity.member.Member;
import neo.chat.api.persistence.repository.member.MemberRepository;
import neo.chat.api.settings.exception.model.authentication.AuthDataMismatchException;
import neo.chat.api.settings.exception.model.authentication.UserNotFoundException;
import neo.chat.api.settings.exception.model.authentication.UsernameDuplicatedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SimpleMemberAuthService implements MemberAuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Member register(String username, String password) throws UsernameDuplicatedException {
        return memberRepository.save(new Member(username, passwordEncoder.encode(password)));
    }

    @Override
    @Transactional(readOnly = true)
    public Member matchUserData(String username, String password)
            throws UserNotFoundException, AuthDataMismatchException {
        Member member = memberRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(password, member.getPassword())) {
            return member;
        }
        throw new AuthDataMismatchException();
    }

    @Override
    @Transactional(readOnly = true)
    public Member getUserDataById(long userId) {
        return memberRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

}
