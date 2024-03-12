package neo.chat.presentation.auth.dto.response;

import neo.chat.persistence.entity.member.Member;

import java.time.Instant;

public record MemberAuthInfoDto(
        long id,
        String username,
        Instant createdAt
) {
    public MemberAuthInfoDto(Member member) {
        this(member.getId(), member.getUsername(), member.getCreatedAt());
    }
}
