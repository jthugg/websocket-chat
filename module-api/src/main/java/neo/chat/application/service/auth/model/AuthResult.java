package neo.chat.application.service.auth.model;

import neo.chat.persistence.entity.member.Member;

public record AuthResult(
        Member member,
        String accessToken,
        String refreshToken,
        long accessTokenTTL,
        long refreshTokenTTL
) {}
