package neo.chat.util;

import neo.chat.persistence.entity.room.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRoomRepository extends JpaRepository<Room, Long> {

    @Query("select room" +
            " from Room room" +
            " join fetch room.participants participant" +
            " where CAST(participant.isHost AS boolean) = true" +
            " and room.removedAt is null" +
            " and participant.removedAt is null")
    List<Room> findByIsHostAndRemovedAtIsNull();

    @Query("select room" +
            " from Room room" +
            " join fetch room.participants participant" +
            " where room.removedAt is null" +
            " and participant.removedAt is null")
    List<Room> findByRemovedAtIsNull();

    @Query("select room" +
            " from Room room" +
            " join fetch room.participants participant" +
            " where room.removedAt is null" +
            " and participant.removedAt is null")
    List<Room> findByRemovedAtIsNullLimit(Pageable pageable);

    @Query("select distinct room" +
            " from Room room" +
            " join fetch room.participants participant" +
            " where room.removedAt is null" +
            " and participant.removedAt is null")
    List<Room> findDistinctByRemovedAtIsNullLimit(Pageable pageable);

}
