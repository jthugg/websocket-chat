package neo.chat.jwt.util;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TokenProperties {

    private final Algorithm algorithm;
    private final long accessTokenTTL;
    private final long refreshTokenTTL;
    private final String usernameKey;

    public TokenProperties(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.accessTokenTTL}") long accessTokenTTL,
            @Value("${jwt.refreshTokenTTL}") long refreshTokenTTL
    ) {
        algorithm = Algorithm.HMAC512(secret);
        this.accessTokenTTL = accessTokenTTL;
        this.refreshTokenTTL = refreshTokenTTL;
        usernameKey = "username";
    }

}
