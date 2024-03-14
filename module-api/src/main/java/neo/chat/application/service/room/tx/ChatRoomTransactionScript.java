package neo.chat.application.service.room.tx;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.entity.room.Room;
import neo.chat.persistence.repository.room.RoomRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChatRoomTransactionScript {

    private final RoomRepository roomRepository;

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

}
