package neo.chat.persistence.query.document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.chat.persistence.query.util.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Getter
@Document(collection = QMember.DOCUMENT_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QMember extends MongoDocument {

    public static final String DOCUMENT_NAME = "Member";

    protected String username;
    @Setter
    protected String password;
    protected String role;

    public QMember(
            UUID id,
            Instant createdAt,
            String username,
            String password,
            String role
    ) {
        super(id, createdAt);
        this.username = username;
        this.password = password;
        this.role = role;
    }

    protected QMember(UUID id) {
        this.id = id;
    }

}
