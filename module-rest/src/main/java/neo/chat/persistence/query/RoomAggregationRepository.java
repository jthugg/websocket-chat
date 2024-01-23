package neo.chat.persistence.query;

import neo.chat.persistence.query.document.QRoom;
import neo.chat.rest.domain.room.dto.request.Search;
import org.springframework.data.domain.Slice;

public interface RoomAggregationRepository {

    Slice<QRoom> searchOnTitle(Search dto);
    Slice<QRoom> searchOnHost(Search dto);

}
