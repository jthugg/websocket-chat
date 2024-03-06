package neo.chat.api.persistence.entity.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neo.chat.api.persistence.entity.util.EntityIDGenerator;
import neo.chat.api.persistence.entity.util.JpaEntity;

@Entity
@Getter
@Table(name = Member.TABLE_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends JpaEntity<Long> {

    public static final String TABLE_NAME = "Member";

    @Id
    protected Long id;
    @Column(unique = true, updatable = false, columnDefinition = "VARCHAR", length = 20)
    protected String username;
    protected String password;
    @Column(columnDefinition = "VARCHAR", length = 20)
    @Enumerated(EnumType.STRING)
    protected MemberRole role;

    public Member(String username, String password) {
        super();
        this.id = EntityIDGenerator.MEMBER.getGenerator().generate().toLong();
        this.username = username;
        this.password = password;
        this.role = MemberRole.ROLE_NORMAL;
    }

}
