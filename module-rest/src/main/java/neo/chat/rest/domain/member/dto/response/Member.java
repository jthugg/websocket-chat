package neo.chat.rest.domain.member.dto.response;

import neo.chat.persistence.command.entity.CMember;
import neo.chat.persistence.query.document.QMember;

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

    public static Member from(QMember member) {
        return new Member(member.getId(), member.getUsername(), member.getCreatedAt());
    }
}
