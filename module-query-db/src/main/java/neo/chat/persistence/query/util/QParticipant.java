package neo.chat.persistence.query.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.chat.persistence.query.document.QMember;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QParticipant {

    @DBRef
    protected QMember member;
    @Setter
    protected Boolean isHost;

    public QParticipant(
            QMember member,
            Boolean isHost
    ) {
        this.member = member;
        this.isHost = isHost;
    }

}
