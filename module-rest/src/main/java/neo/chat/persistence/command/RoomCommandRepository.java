package neo.chat.persistence.command;

import neo.chat.persistence.command.entity.CRoom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomCommandRepository extends CrudRepository<CRoom, UUID> {

    /**
     * 채팅방 정보와 해당 채팅방 참여자 정보 페치 조인
     *
     * @param id room identifier
     * @return CRoom - nullable wrapped Optional
     */
    @Query("select distinct cr from CRoom cr join fetch cr.participants cp join fetch cp.member cm where cr.id = :id")
    Optional<CRoom> findByIdFetchParticipantsFetchMember(@Param("id") UUID id);

}
