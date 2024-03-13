package neo.chat.util;

import neo.chat.persistence.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMemberRepository extends JpaRepository<Member, Long> {
}
