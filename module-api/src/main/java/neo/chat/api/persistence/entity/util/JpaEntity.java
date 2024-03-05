package neo.chat.api.persistence.entity.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class JpaEntity<ID> implements Persistable<ID> {

    @Column(nullable = false, columnDefinition = "TIMESTAMP", length = 3)
    protected Instant createdAt;
    @Column(columnDefinition = "TIMESTAMP", length = 3)
    protected Instant removedAt;

    protected JpaEntity() {
        this.createdAt = Instant.now();
        this.removedAt = null;
    }

    public boolean isRemoved() {
        return this.removedAt != null;
    }

    public Instant remove() {
        this.removedAt = Instant.now();
        return removedAt;
    }

    @Override
    public boolean isNew() {
        return true;
    }

}
