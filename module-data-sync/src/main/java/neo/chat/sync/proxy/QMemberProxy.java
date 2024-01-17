package neo.chat.sync.proxy;

import neo.chat.persistence.query.document.QMember;

import java.util.UUID;

public class QMemberProxy extends QMember {

    public QMemberProxy(UUID id) {
        super(id);
    }

}
