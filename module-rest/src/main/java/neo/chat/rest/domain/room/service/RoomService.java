package neo.chat.rest.domain.room.service;

import neo.chat.persistence.command.entity.CRoom;
import neo.chat.rest.domain.room.dto.request.Create;

public interface RoomService {

    CRoom create(Create dto);

}
