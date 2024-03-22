package neo.chat.persistence.repository.room;

import neo.chat.persistence.entity.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByIdAndRemovedAtIsNull(Long id);

}
