package neo.chat.application.service.auth.service;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import neo.chat.application.service.auth.exception.MemberPasswordNotMatchedException;
import neo.chat.application.service.auth.model.AuthResult;
import neo.chat.application.service.auth.properties.JWTProperties;
import neo.chat.application.service.auth.tx.MemberAuthTransactionScript;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.repository.member.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class SimpleMemberAuthService implements MemberAuthService {

    private final MemberRepository memberRepository;
    private final MemberAuthTransactionScript transactionScript;
    private final JWTProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return !memberRepository.existsByUsername(username);
    }

    @Override
    public AuthResult register(String username, String password) {
        return publishAuthResult(transactionScript.createMember(username, password));
    }

    @Override
    public AuthResult login(String username, String password) {
        Member member = transactionScript.readMemberByUsername(username);
        if (passwordEncoder.matches(password, member.getPassword())) {
            return publishAuthResult(member);
        }
        throw new MemberPasswordNotMatchedException();
    }

    private AuthResult publishAuthResult(Member member) {
        Instant now = Instant.now();
        return new AuthResult(
                member,
                JWT.create()
                        .withClaim(JWTProperties.USER_ID, member.getId())
                        .withClaim(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                        .withExpiresAt(now.plus(jwtProperties.atkTTL(), ChronoUnit.SECONDS))
                        .sign(jwtProperties.algorithm()),
                JWT.create()
                        .withClaim(JWTProperties.USER_ID, member.getId())
                        .withClaim(JWTProperties.TYPE, JWTProperties.REFRESH_TOKEN)
                        .withExpiresAt(now.plus(jwtProperties.rtkTTL(), ChronoUnit.SECONDS))
                        .sign(jwtProperties.algorithm()),
                jwtProperties.atkTTL(),
                jwtProperties.rtkTTL()
        );
    }

}
