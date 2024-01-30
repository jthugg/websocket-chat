package neo.chat.persistence.query.document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neo.chat.persistence.query.util.MongoDocument;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Getter
@Document(collection = QMessage.DOCUMENT_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QMessage extends MongoDocument {

    public static final String DOCUMENT_NAME = "Message";

    @DBRef
    protected QMember sender;
    @DBRef
    protected QRoom room;
    protected String content;

    public QMessage(
            UUID id,
            Instant createdAt,
            QMember sender,
            QRoom room,
            String content
    ) {
        super(id, createdAt);
        this.sender = sender;
        this.room = room;
        this.content = content;
    }

    protected QMessage(UUID id) {
        this.id = id;
    }

}
