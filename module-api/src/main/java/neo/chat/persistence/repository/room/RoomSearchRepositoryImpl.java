package neo.chat.persistence.repository.room;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import neo.chat.application.service.room.model.SearchChatRoomRequest;
import neo.chat.persistence.entity.room.Room;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;

public class RoomSearchRepositoryImpl extends SimpleJpaRepository<Room, Long> implements RoomSearchRepository {

    public RoomSearchRepositoryImpl(EntityManager entityManager) {
        super(Room.class, entityManager);
    }

    @Override
    public List<Room> searchRoom(SearchChatRoomRequest request) {
        TypedQuery<Room> query = getQuery(RoomSearchSpecification.getSpecification(request), request.getPageable());
        query.setMaxResults(request.size());
        return query.getResultList();
    }

}
