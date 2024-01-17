package neo.chat.sync.domain.participant.service;

import neo.chat.persistence.command.entity.CParticipant;

import java.util.UUID;

public interface ParticipantSyncService {

    void create(UUID targetId, CParticipant participant);
    void update(UUID targetId, CParticipant before, CParticipant after);
    void delete(UUID targetId, CParticipant participant);

}
