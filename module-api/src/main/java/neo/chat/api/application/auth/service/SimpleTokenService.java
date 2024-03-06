package neo.chat.api.application.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import neo.chat.api.application.auth.model.TokenSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class SimpleTokenService implements TokenService {

    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;
    private final long atkTTL;
    private final long rtkTTL;

    @Autowired
    public SimpleTokenService(
            Algorithm algorithm,
            JWTVerifier jwtVerifier,
            @Value("${jwt.atkTTL}") long atkTTL,
            @Value("${jwt.rtkTTL}") long rtkTTL
    ) {
        this.algorithm = algorithm;
        this.jwtVerifier = jwtVerifier;
        this.atkTTL = atkTTL;
        this.rtkTTL = rtkTTL;
    }

    @Override
    public TokenSet publish(String userId) {
        Instant now = Instant.now();
        Instant accessTokenExpiresAt = now.plus(Duration.of(atkTTL, ChronoUnit.SECONDS));
        Instant refreshTokenExpiresAt = now.plus(Duration.of(rtkTTL, ChronoUnit.SECONDS));
        return new TokenSet(
                JWT.create()
                        .withSubject(ACCESS_TOKEN_SUBJECT)
                        .withClaim(USER_ID_CLAIM, userId)
                        .withExpiresAt(accessTokenExpiresAt)
                        .sign(algorithm),
                JWT.create()
                        .withSubject(REFRESH_TOKEN_SUBJECT)
                        .withClaim(USER_ID_CLAIM, userId)
                        .withExpiresAt(refreshTokenExpiresAt)
                        .sign(algorithm),
                accessTokenExpiresAt,
                refreshTokenExpiresAt
        );
    }

    @Override
    public DecodedJWT resolve(String token) throws JWTDecodeException {
        return JWT.decode(token);
    }

    @Override
    public DecodedJWT verify(String token) throws JWTVerificationException {
        return jwtVerifier.verify(token);
    }

}
