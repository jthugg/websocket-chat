package neo.chat.persistence.command;

import neo.chat.persistence.command.entity.CParticipant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParticipantCommandRepository extends CrudRepository<CParticipant, UUID> {
}
