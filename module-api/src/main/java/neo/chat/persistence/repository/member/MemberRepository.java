package neo.chat.persistence.repository.member;

import neo.chat.persistence.entity.member.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

    boolean existsByUsername(String username);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByIdAndRemovedAtIsNull(Long id);

}
