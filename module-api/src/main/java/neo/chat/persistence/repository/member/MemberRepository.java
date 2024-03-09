package neo.chat.persistence.repository.member;

import neo.chat.persistence.entity.member.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

    boolean existsByUsername(String username);

}