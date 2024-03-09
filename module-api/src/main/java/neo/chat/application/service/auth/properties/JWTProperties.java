package neo.chat.application.service.auth.properties;

import com.auth0.jwt.algorithms.Algorithm;

public record JWTProperties(
        Algorithm algorithm,
        long atkTTL,
        long rtkTTL
) {

    public static final String USER_ID = "uid";

}
