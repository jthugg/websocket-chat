package neo.chat.rest.domain.room.service;

import neo.chat.persistence.query.document.QRoom;
import neo.chat.rest.domain.room.dto.request.Search;
import org.springframework.data.domain.Slice;

public interface RoomSearchService {

    Slice<QRoom> search(Search dto);

}
