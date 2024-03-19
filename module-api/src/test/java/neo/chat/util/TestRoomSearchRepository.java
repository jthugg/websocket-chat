package neo.chat.util;

import neo.chat.persistence.entity.room.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TestRoomSearchRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

    List<Room> searchRoom(Specification<Room> spec, Pageable pageable);

}
