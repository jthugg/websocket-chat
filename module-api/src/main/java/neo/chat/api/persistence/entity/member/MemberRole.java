package neo.chat.api.persistence.entity.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    ROLE_NORMAL("ROLE_NORMAL"),
    ROLE_PREMIUM("ROLE_PREMIUM"),
    ;

    private final String name;

}
