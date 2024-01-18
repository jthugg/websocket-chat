package neo.chat.rest.domain.room.service;

import neo.chat.persistence.command.entity.CRoom;
import neo.chat.rest.domain.room.dto.request.Create;
import neo.chat.rest.domain.room.dto.request.Enter;
import neo.chat.rest.domain.room.dto.request.Update;

import java.util.UUID;

public interface RoomService {

    CRoom create(Create dto);
    CRoom enter(UUID targetId, Enter dto);
    void leave(UUID targetId);
    CRoom update(UUID targetId, Update dto);

}
