package neo.chat.websocket.util;

import neo.chat.persistence.query.document.QMember;

import java.security.Principal;

public class ChatUser implements Principal {

    private final QMember member;

    public ChatUser(QMember member) {
        this.member = member;
    }

    @Override
    public String getName() {
        return member.getId().toString();
    }

}
