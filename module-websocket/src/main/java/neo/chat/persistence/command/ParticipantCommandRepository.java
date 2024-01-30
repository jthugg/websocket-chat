package neo.chat.persistence.command;

import neo.chat.persistence.command.entity.CParticipant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipantCommandRepository extends CrudRepository<CParticipant, UUID> {

    @Query("select p" +
            " from CParticipant p" +
            " join fetch p.member m" +
            " join fetch p.room r" +
            " where p.member.id = :memberId and p.room.id = :roomId and p.removedAt is null")
    Optional<CParticipant> findByRoomIdAndMemberId(@Param("memberId") UUID memberId, @Param("roomId") UUID roomId);

}
