package neo.chat.sync.domain.room.service;

import lombok.RequiredArgsConstructor;
import neo.chat.lock.annotation.DistributedLock;
import neo.chat.lock.annotation.LockType;
import neo.chat.lock.annotation.TargetDataSource;
import neo.chat.persistence.command.ParticipantCommandRepository;
import neo.chat.persistence.command.entity.CRoom;
import neo.chat.persistence.query.RoomQueryRepository;
import neo.chat.persistence.query.document.QRoom;
import neo.chat.persistence.query.util.QParticipant;
import neo.chat.sync.proxy.QMemberProxy;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimpleRoomSyncService implements RoomSyncService {

    private final ParticipantCommandRepository participantCommandRepository;
    private final RoomQueryRepository roomQueryRepository;

    @Override
    @DistributedLock(type = LockType.CHAT_ROOM, targetDataSource = TargetDataSource.QUERY)
    public void create(UUID targetId, CRoom room) {
        participantCommandRepository.findByRoomIdFetchMember(targetId).ifPresent(participant -> {
            QMemberProxy memberProxy = new QMemberProxy(participant.getMember().getId());
            assert participant.getId() != null;
            roomQueryRepository.save(new QRoom(
                    room.getId(),
                    room.getCreatedAt(),
                    room.getTitle(),
                    room.getCapacity(),
                    room.getPassword(),
                    memberProxy,
                    Map.of(participant.getId(), new QParticipant(
                            memberProxy,
                            participant.getIsHost(),
                            participant.getCreatedAt()
                    ))
            ));
        });
    }

    @Override
    @DistributedLock(type = LockType.CHAT_ROOM, targetDataSource = TargetDataSource.QUERY)
    public void update(UUID targetId, CRoom before, CRoom after) {
        roomQueryRepository.findById(targetId).ifPresent(qRoom -> {
            qRoom.setTitle(after.getTitle());
            qRoom.setCapacity(after.getCapacity());
            qRoom.setPassword(after.getPassword());
            qRoom.setHost(new QMemberProxy(after.getHost().getId()));
            roomQueryRepository.save(qRoom);
        });
    }

    @Override
    @DistributedLock(type = LockType.CHAT_ROOM, targetDataSource = TargetDataSource.QUERY)
    public void delete(UUID targetId) {
        roomQueryRepository.deleteById(targetId);
    }

}
