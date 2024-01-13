package neo.chat.persistence.command.entity;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.UUIDUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.chat.persistence.command.util.JpaEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity
@Getter
@Builder
@Table(name = CRoom.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CRoom extends JpaEntity {

    public static final String TABLE_NAME = "Room";

    @Setter
    protected String title;
    @Setter
    protected int capacity;
    @Setter
    protected String password;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host")
    protected CMember host;
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room", cascade = CascadeType.ALL)
    protected List<CParticipant> participants = new ArrayList<>();

    public CRoom(String base64Id) {
        // constructor for kafka deserializer
        id = UUIDUtil.uuid(Base64.getDecoder().decode(base64Id));
    }

    @PrePersist
    public void prePersist() {
        id = Generators.timeBasedGenerator().generate();
        createdAt = Instant.now();
    }

}
