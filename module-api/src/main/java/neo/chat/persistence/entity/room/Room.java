package neo.chat.persistence.entity.room;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neo.chat.persistence.entity.participant.Participant;
import neo.chat.persistence.entity.util.JpaEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = Room.TABLE_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends JpaEntity<Long> {

    public static final String TABLE_NAME = "Room";
    public static final String HOST_COLUMN_NAME = "host";

    @Id
    private Long id;
    private String title;
    private String password;
    private Integer capacity;
    private Integer attending;
    private Integer saturation; // saturationValue = (100 * attendingValue) / capacityValue

    @OneToMany(fetch = FetchType.LAZY, mappedBy = Participant.ROOM_COLUMN_NAME, cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();

    @Builder
    public Room(Long id, String title, String password, int capacity) {
        this.id = id;
        this.title = title;
        this.password = password;
        this.capacity = capacity;
        this.attending = 1;
        this.saturation = (100 * attending) / capacity;
    }

}
