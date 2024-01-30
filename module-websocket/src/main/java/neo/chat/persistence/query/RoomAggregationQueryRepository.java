package neo.chat.persistence.query;

import java.util.UUID;

public interface RoomAggregationQueryRepository {

    boolean checkSubscribable(UUID roomId, UUID memberId);

}
