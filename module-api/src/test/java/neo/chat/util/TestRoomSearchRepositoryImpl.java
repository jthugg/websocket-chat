package neo.chat.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import neo.chat.persistence.entity.room.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;

public class TestRoomSearchRepositoryImpl extends SimpleJpaRepository<Room, Long> implements TestRoomSearchRepository {

    public TestRoomSearchRepositoryImpl(EntityManager entityManager) {
        super(Room.class, entityManager);
    }

    @Override
    public List<Room> searchRoom(Specification<Room> spec, Pageable pageable) {
        TypedQuery<Room> query = getQuery(spec, pageable);
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

}
