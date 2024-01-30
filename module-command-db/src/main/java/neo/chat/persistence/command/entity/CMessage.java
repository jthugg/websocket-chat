package neo.chat.persistence.command.entity;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.UUIDUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neo.chat.persistence.command.util.JpaEntity;

import java.time.Instant;
import java.util.Base64;

@Entity
@Getter
@Builder
@Table(name = CMessage.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CMessage extends JpaEntity {

    public static final String TABLE_NAME = "Message";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender")
    protected CMember sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room")
    protected CRoom room;
    protected String content;

    public CMessage(String base64Id) {
        // constructor for kafka deserializer
        id = UUIDUtil.uuid(Base64.getDecoder().decode(base64Id));
    }

    @PrePersist
    public void prePersist() {
        id = Generators.timeBasedGenerator().generate();
        createdAt = Instant.now();
    }

}
