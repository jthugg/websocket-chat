package neo.chat.persistence.query;

import neo.chat.persistence.query.document.QMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageQueryRepository extends CrudRepository<QMessage, UUID> {
}
