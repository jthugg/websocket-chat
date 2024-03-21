package neo.chat.persistence.repository.room;

import neo.chat.application.service.room.model.SearchChatRoomRequest;
import neo.chat.persistence.entity.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomSearchRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

    List<Room> searchRoom(SearchChatRoomRequest request);

}
