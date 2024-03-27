package neo.chat.application.service.room.service;

import neo.chat.application.service.room.model.ChangeChatRoomInfoRequest;
import neo.chat.application.service.room.model.EnterChatRoomRequest;
import neo.chat.application.service.room.model.OpenChatRoomRequest;
import neo.chat.application.service.room.model.SearchChatRoomRequest;
import neo.chat.persistence.entity.participant.Participant;
import neo.chat.persistence.entity.room.Room;

import java.util.List;

public interface ChatRoomService {

    Room openChatRoom(OpenChatRoomRequest request);
    List<Room> searchChatRoom(SearchChatRoomRequest request);
    List<Participant> getParticipatingRooms(Long cursorId, int fetchSize);
    Room getRoomData(Long id);
    Room enterRoom(EnterChatRoomRequest request);
    void leaveRoom(Long id);
    List<Participant> changeHost(Long roomId, Long myParticipantId, Long targetParticipantId);
    Room changeRoomInfo(ChangeChatRoomInfoRequest request);

}
