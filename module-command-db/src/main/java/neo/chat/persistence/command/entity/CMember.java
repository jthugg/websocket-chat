package neo.chat.persistence.command.entity;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.UUIDUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.chat.persistence.command.util.JpaEntity;
import neo.chat.persistence.command.util.Role;

import java.time.Instant;
import java.util.Base64;

@Entity
@Getter
@Builder
@Table(name = CMember.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CMember extends JpaEntity {

    public static final String TABLE_NAME = "Member";

    @Column(unique = true)
    protected String username;
    @Setter
    protected String password;
    @Enumerated(EnumType.STRING)
    protected Role role;

    public CMember(String base64Id) {
        // constructor for kafka deserializer
        id = UUIDUtil.uuid(Base64.getDecoder().decode(base64Id));
    }

    @PrePersist
    public void prePersist() {
        id = Generators.timeBasedGenerator().generate();
        createdAt = Instant.now();
        if (role == null) {
            role = Role.ROLE_MEMBER;
        }
    }

}
