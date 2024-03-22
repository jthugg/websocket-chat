package neo.chat.application.service.room.tx;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.entity.participant.Participant;
import neo.chat.persistence.entity.room.Room;
import neo.chat.persistence.repository.participant.ParticipantRepository;
import neo.chat.persistence.repository.room.RoomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class ChatRoomTransactionScript {

    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public List<Participant> getParticipatingRooms(Member member, Pageable pageable) {
        return participantRepository.findByMemberAndRemovedAtIsNull(member, pageable);
    }

    @Transactional(readOnly = true)
    public List<Participant> getParticipatingRooms(Member member, Long cursorId, Pageable pageable) {
        return participantRepository.findByMemberAndIdLessThanAndRemovedAtIsNull(member, cursorId, pageable);
    }

}
