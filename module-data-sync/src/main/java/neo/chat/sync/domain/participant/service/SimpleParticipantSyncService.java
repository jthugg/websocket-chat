package neo.chat.sync.domain.participant.service;

import lombok.RequiredArgsConstructor;
import neo.chat.lock.annotation.DistributedLock;
import neo.chat.lock.annotation.LockType;
import neo.chat.lock.annotation.TargetDataSource;
import neo.chat.persistence.command.entity.CParticipant;
import neo.chat.persistence.query.RoomQueryRepository;
import neo.chat.persistence.query.util.QParticipant;
import neo.chat.sync.proxy.QMemberProxy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimpleParticipantSyncService implements ParticipantSyncService {

    private final RoomQueryRepository roomQueryRepository;

    @Override
    @DistributedLock(type = LockType.CHAT_ROOM, targetDataSource = TargetDataSource.QUERY)
    public void create(UUID targetId, CParticipant participant) {
        if (!participant.getIsHost()) { // 방 생성시 호스트가 채팅방에 입장하는 경우 채팅방 동기화 과정에서 처리
            assert participant.getRoom().getId() != null;
            roomQueryRepository.findById(participant.getRoom().getId()).ifPresent(room -> {
                room.getParticipants().put(
                        participant.getId(),
                        new QParticipant(
                                new QMemberProxy(participant.getMember().getId()),
                                participant.getIsHost(),
                                participant.getCreatedAt()
                        )
                );
                roomQueryRepository.save(room);
            });
        }
    }

    @Override
    @DistributedLock(type = LockType.CHAT_ROOM, targetDataSource = TargetDataSource.QUERY)
    public void update(UUID targetId, CParticipant before, CParticipant after) {
        assert before.getRoom().getId() != null;
        roomQueryRepository.findById(before.getRoom().getId()).ifPresent(room -> {
            room.getParticipants().get(before.getId()).setIsHost(after.getIsHost());
            roomQueryRepository.save(room);
        });
    }

    @Override
    @DistributedLock(type = LockType.CHAT_ROOM, targetDataSource = TargetDataSource.QUERY)
    public void delete(UUID targetId, CParticipant participant) {
        assert participant.getRoom().getId() != null;
        roomQueryRepository.findById(participant.getRoom().getId()).ifPresent(room -> {
            room.getParticipants().remove(participant.getId());
            roomQueryRepository.save(room);
        });
    }

}
