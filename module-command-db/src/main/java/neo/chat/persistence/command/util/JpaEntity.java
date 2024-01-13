package neo.chat.persistence.command.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class JpaEntity implements Persistable<UUID> {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    protected UUID id;
    protected Instant createdAt;
    protected Instant removedAt;

    public boolean isRemoved() {
        return this.removedAt != null;
    }

    @Override
    public boolean isNew() {
        return true;
    }

}
