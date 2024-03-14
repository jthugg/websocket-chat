package neo.chat.application.service.room.service;

import neo.chat.application.service.room.model.OpenChatRoomRequest;
import neo.chat.persistence.entity.room.Room;

public interface ChatRoomService {

    Room openChatRoom(OpenChatRoomRequest request);

}
