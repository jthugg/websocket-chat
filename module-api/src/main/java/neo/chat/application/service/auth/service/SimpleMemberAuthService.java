package neo.chat.application.service.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import neo.chat.application.service.auth.exception.InvalidTokenException;
import neo.chat.application.service.auth.exception.MemberNotFoundException;
import neo.chat.application.service.auth.exception.MemberPasswordNotMatchedException;
import neo.chat.application.service.auth.model.AuthResult;
import neo.chat.application.service.auth.model.ChatUserDetails;
import neo.chat.application.service.auth.properties.JWTProperties;
import neo.chat.application.service.auth.tx.MemberAuthTransactionScript;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.repository.member.MemberRepository;
import neo.chat.settings.context.AuthMemberContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final JWTVerifier jwtVerifier;

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

    @Override
    public AuthResult reissue(String refreshToken) {
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
            if (decodedJWT.getClaim(JWTProperties.TYPE).asString().equals(JWTProperties.REFRESH_TOKEN)) {
                long memberId = decodedJWT.getClaim(JWTProperties.USER_ID).asLong();
                return publishAuthResult(transactionScript.readMemberById(memberId));
            }
            throw new InvalidTokenException();
        } catch (JWTVerificationException exception) {
            throw new InvalidTokenException();
        }
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

    @Override
    public UserDetails authorization(String accessToken) {
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(accessToken);
            if (decodedJWT.getClaim(JWTProperties.TYPE).asString().equals(JWTProperties.ACCESS_TOKEN)) {
                long memberId = decodedJWT.getClaim(JWTProperties.USER_ID).asLong();
                Member member = transactionScript.readMemberById(memberId);
                AuthMemberContextHolder.set(member);
                return new ChatUserDetails(member);
            }
            throw new InvalidTokenException();
        } catch (JWTVerificationException exception) {
            throw new InvalidTokenException();
        }
    }

    /**
     * 사용하지 않는 기능.<br />
     * 더미 유저 생성을 비활성화 하기 위해 UserDetailsService 를 구현합니다.<br />
     * MemberAuthService.authorization(String accessToken) 로 대체
     *
     * @throws UnsupportedOperationException 항상 예외 발생
     * @deprecated since 1.0.0
     */
    @Override
    @Deprecated(since = "1.0.0", forRemoval = true)
    public UserDetails loadUserByUsername(String username) {
        throw new UnsupportedOperationException();
    }

}
