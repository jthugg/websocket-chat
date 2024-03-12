package neo.chat.persistence.entity.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.chat.persistence.entity.util.JpaEntity;

@Entity
@Getter
@Table(name = Member.TABLE_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends JpaEntity<Long> {

    public static final String TABLE_NAME = "Member";

    @Id
    private Long id;
    @Column(unique = true, updatable = false, columnDefinition = "VARCHAR", length = 20)
    private String username;
    @Setter
    private String password;
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR", length = 20)
    private MemberRole role;

    public Member(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = MemberRole.ROLE_NORMAL;
    }

}
