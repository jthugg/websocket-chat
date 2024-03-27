package neo.chat.persistence.entity.participant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.entity.room.Room;
import neo.chat.persistence.entity.util.JpaEntity;

@Entity
@Getter
@Table(name = Participant.TABLE_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant extends JpaEntity<Long> {

    public static final String TABLE_NAME = "Participant";
    public static final String MEMBER_COLUMN_NAME = "member";
    public static final String ROOM_COLUMN_NAME = "room";

    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = MEMBER_COLUMN_NAME)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ROOM_COLUMN_NAME)
    private Room room;
    @Column(columnDefinition = "TINYINT", length = 1)
    private Boolean isHost;
    private String nickname;

    @Builder
    public Participant(Long id, Member member, Room room, boolean isHost, String nickname) {
        this.id = id;
        this.member = member;
        this.room = room;
        this.isHost = isHost;
        this.nickname = nickname;
    }

    public void changeHost(Participant target) {
        this.isHost = false;
        target.isHost = true;
    }

}
