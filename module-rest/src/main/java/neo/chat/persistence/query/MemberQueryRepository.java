package neo.chat.persistence.query;

import neo.chat.persistence.query.document.QMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberQueryRepository extends CrudRepository<QMember, UUID> {

    boolean existsByUsername(String username);
    Optional<QMember> findByUsername(String username);

}
