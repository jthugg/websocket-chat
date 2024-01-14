package neo.chat.rest.domain.member.dto.response;

import neo.chat.persistence.command.entity.CMember;

import java.time.Instant;
import java.util.UUID;

public record Member(
        UUID id,
        String username,
        Instant createdAt
) {
    public static Member from(CMember member) {
        return new Member(member.getId(), member.getUsername(), member.getCreatedAt());
    }
}
