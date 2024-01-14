package neo.chat.jwt.model;

public record TokenSet(
        String accessToken,
        String refreshToken
) {
}
