package neo.chat.rest.domain.room.dto.response;

import neo.chat.persistence.query.document.QRoom;
import org.springframework.data.domain.Slice;

import java.util.List;

public record SearchResult(
        List<Room> rooms,
        boolean hasNext
) {
    public static SearchResult from(Slice<QRoom> results) {
        return new SearchResult(results.getContent().stream().map(Room::asSummary).toList(), results.hasNext());
    }
}
