package neo.chat.sync.domain.room.service;

import neo.chat.persistence.command.entity.CRoom;

import java.util.UUID;

public interface RoomSyncService {

    void create(UUID targetId, CRoom room);
    void update(UUID targetId, CRoom before, CRoom after);
    void delete(UUID targetId);

}
