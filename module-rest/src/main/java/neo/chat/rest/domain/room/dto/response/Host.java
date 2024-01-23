package neo.chat.rest.domain.room.dto.response;

import neo.chat.persistence.command.entity.CMember;
import neo.chat.persistence.query.document.QMember;

import java.util.UUID;

public record Host(
        UUID id,
        String username
) {
    public static Host from(CMember member) {
        return new Host(member.getId(), member.getUsername());
    }

    public static Host from(QMember member) {
        return new Host(member.getId(), member.getUsername());
    }
}
