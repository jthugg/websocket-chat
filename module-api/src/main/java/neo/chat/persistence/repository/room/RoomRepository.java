package neo.chat.persistence.repository.room;

import jakarta.persistence.LockModeType;
import neo.chat.persistence.entity.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByIdAndRemovedAtIsNull(Long id);

    @Query("select room" +
            " from Room room" +
            " join fetch room.participants participant" +
            " where room.id = :id" +
                " and room.removedAt is null" +
                " and participant.removedAt is null")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Room> findByIdJoinFetchParticipantsWithLock(Long id);

    @Query("select room" +
            " from Room room" +
            " join fetch room.participants participant" +
            " join fetch participant.member member" +
            " where room.id = :roomId" +
                " and room.removedAt is null" +
                " and CAST(participant.isHost as boolean) = true" +
                " and participant.removedAt is null" +
                " and member.id = :memberId" +
                " and member.removedAt is null")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Room> findRoomDataWithLockForUpdate(Long roomId, Long memberId);

}
