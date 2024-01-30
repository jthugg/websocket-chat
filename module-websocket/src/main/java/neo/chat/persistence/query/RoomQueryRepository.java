package neo.chat.persistence.query;

import neo.chat.persistence.query.document.QRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoomQueryRepository extends CrudRepository<QRoom, UUID>, RoomAggregationQueryRepository {
}
