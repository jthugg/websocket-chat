package neo.chat.settings.bean;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import neo.chat.application.service.auth.properties.JWTProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfig {

    private final Algorithm algorithm;
    private final long atkTTL;
    private final long rtkTTL;

    public JWTConfig(
            @Value("${token.secret}") String secret,
            @Value("${token.atkTTL}") long atkTTL,
            @Value("${token.rtkTTL}") long rtkTTL
    ) {
        this.algorithm = Algorithm.HMAC512(secret);
        this.atkTTL = atkTTL;
        this.rtkTTL = rtkTTL;
    }

    @Bean
    public JWTProperties jwtProperties() {
        return new JWTProperties(algorithm, atkTTL, rtkTTL);
    }

    @Bean
    public JWTVerifier jwtVerifier() {
        return JWT.require(algorithm).build();
    }

}
