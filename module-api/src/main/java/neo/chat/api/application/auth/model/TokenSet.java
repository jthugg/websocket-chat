package neo.chat.api.application.auth.model;

import java.time.Instant;

public record TokenSet(
        String accessToken,
        String refreshToken,
        Instant accessTokenExpiresAt,
        Instant refreshTokenExpiresAt
) {}
