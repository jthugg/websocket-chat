package neo.chat.rest.domain.room.service;

import neo.chat.persistence.command.entity.CRoom;
import neo.chat.rest.domain.room.dto.request.Create;

import java.util.UUID;

public interface RoomService {

    CRoom create(Create dto);
    CRoom enter(UUID targetRoomId);

}
