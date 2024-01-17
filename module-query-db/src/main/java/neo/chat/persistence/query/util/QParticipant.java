package neo.chat.persistence.query.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.chat.persistence.query.document.QMember;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QParticipant {

    @DBRef
    protected QMember member;
    @Setter
    protected Boolean isHost;
    protected Instant joinAt;

    public QParticipant(
            QMember member,
            Boolean isHost,
            Instant joinAt
    ) {
        this.member = member;
        this.isHost = isHost;
        this.joinAt = joinAt;
    }

}
