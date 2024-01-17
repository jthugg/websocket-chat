package neo.chat.persistence.query.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.chat.persistence.query.util.MongoDocument;
import neo.chat.persistence.query.util.QParticipant;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Document(collection = QRoom.DOCUMENT_NAME)
@NoArgsConstructor
public class QRoom extends MongoDocument {

    public static final String DOCUMENT_NAME = "Room";

    @Setter
    protected String title;
    @Setter
    protected int capacity;
    @Setter
    protected String password;
    @DBRef
    @Setter
    protected QMember host;
    protected Map<UUID, QParticipant> participants;

    public QRoom(
            UUID id,
            Instant createdAt,
            String title,
            int capacity,
            String password,
            QMember host,
            Map<UUID, QParticipant> participants
    ) {
        super(id, createdAt);
        this.title = title;
        this.capacity = capacity;
        this.password = password;
        this.host = host;
        this.participants = participants;
    }

    protected QRoom(UUID id) {
        this.id = id;
    }

}
