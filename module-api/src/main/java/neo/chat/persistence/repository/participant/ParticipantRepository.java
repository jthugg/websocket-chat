package neo.chat.persistence.repository.participant;

import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.entity.participant.Participant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("select participant" +
            " from Participant participant" +
            " join fetch participant.room" +
            " where participant.member = :member" +
            " and participant.removedAt is null")
    List<Participant> findByMemberAndRemovedAtIsNull(Member member, Pageable pageable);

    @Query("select participant" +
            " from Participant participant" +
            " join fetch participant.room" +
            " where participant.member = :member" +
            " and participant.id < :cursorId" +
            " and participant.removedAt is null")
    List<Participant> findByMemberAndIdLessThanAndRemovedAtIsNull(Member member, Long cursorId, Pageable pageable);

}
