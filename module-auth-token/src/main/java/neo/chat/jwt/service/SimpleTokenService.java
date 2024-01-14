package neo.chat.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.JWTVerifier;
import neo.chat.jwt.model.TokenSet;
import neo.chat.jwt.util.InvalidTokenException;
import neo.chat.jwt.util.TokenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class SimpleTokenService implements TokenService {

    private final TokenProperties tokenProperties;
    private final JWTVerifier jwtVerifier;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public SimpleTokenService(
            TokenProperties tokenProperties,
            RedisTemplate<String, String> redisTemplate
    ) {
        this.tokenProperties = tokenProperties;
        jwtVerifier = JWT.require(tokenProperties.getAlgorithm()).build();
        this.redisTemplate = redisTemplate;
    }

    @Override
    public TokenSet generateAccessToken(String username) {
        Instant now = Instant.now();
        return new TokenSet(
                JWT.create()
                        .withClaim(tokenProperties.getUsernameKey(), username)
                        .withExpiresAt(now.plusSeconds(tokenProperties.getAccessTokenTTL()))
                        .sign(tokenProperties.getAlgorithm()),
                JWT.create()
                        .withClaim(tokenProperties.getUsernameKey(), username)
                        .withExpiresAt(now.plusSeconds(tokenProperties.getRefreshTokenTTL()))
                        .sign(tokenProperties.getAlgorithm())
        );
    }

    @Override
    public boolean isExpired(String token) throws InvalidTokenException {
        try {
            return jwtVerifier.verify(token).getExpiresAtAsInstant().isBefore(Instant.now())
                    || Boolean.TRUE.equals(redisTemplate.hasKey(token));
        } catch (TokenExpiredException exception) {
            return true;
        } catch (JWTVerificationException exception) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public String getUsername(String accessToken) throws InvalidTokenException {
        try {
            return JWT.decode(accessToken).getClaim(tokenProperties.getUsernameKey()).asString();
        } catch (JWTDecodeException exception) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public void blacklist(String... tokens) {
        for (String token : tokens) {
            try {
                long ttl = jwtVerifier.verify(token)
                        .getExpiresAtAsInstant()
                        .minusSeconds(Instant.now().getEpochSecond())
                        .toEpochMilli();
                if (ttl > 0) {
                    redisTemplate.opsForValue().set(token, "", ttl, TimeUnit.MILLISECONDS);
                }
            } catch (JWTVerificationException exception) {
                // ignore
                // don't need to blacklist expired or not verified token
            }
        }
    }

}
