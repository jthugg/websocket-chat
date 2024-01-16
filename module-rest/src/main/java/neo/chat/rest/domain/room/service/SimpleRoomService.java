package neo.chat.rest.domain.room.service;

import lombok.RequiredArgsConstructor;
import neo.chat.lock.annotation.DistributedLock;
import neo.chat.lock.annotation.LockType;
import neo.chat.lock.annotation.TargetDataSource;
import neo.chat.persistence.command.MemberCommandRepository;
import neo.chat.persistence.command.RoomCommandRepository;
import neo.chat.persistence.command.config.CommandDBConfig;
import neo.chat.persistence.command.entity.CMember;
import neo.chat.persistence.command.entity.CParticipant;
import neo.chat.persistence.command.entity.CRoom;
import neo.chat.rest.domain.room.dto.request.Create;
import neo.chat.rest.domain.room.exception.RoomException;
import neo.chat.security.util.SecurityUserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimpleRoomService implements RoomService {

    private final RoomCommandRepository roomCommandRepository;
    private final MemberCommandRepository memberCommandRepository;

    @Override
    @Transactional(transactionManager = CommandDBConfig.TRANSACTION_MANAGER)
    public CRoom create(Create dto) {
        CMember member = memberCommandRepository.findById(SecurityUserContextHolder.get().getId())
                .orElseThrow(RoomException.MemberNotFoundException::new);
        CRoom room = CRoom.builder()
                .title(dto.title())
                .capacity(dto.capacity())
                .password(dto.password())
                .host(member)
                .build();
        CParticipant participant = CParticipant.builder()
                .member(member)
                .room(room)
                .isHost(true)
                .build();
        room.getParticipants().add(participant);
        return roomCommandRepository.save(room);
    }

    @Override
    @DistributedLock(type = LockType.CHAT_ROOM, targetDataSource = TargetDataSource.COMMAND)
    public CRoom enter(UUID targetId) {
        CMember member = memberCommandRepository.findById(SecurityUserContextHolder.get().getId())
                .orElseThrow(RoomException.MemberNotFoundException::new);
        CRoom room = roomCommandRepository.findByIdFetchParticipantsFetchMember(targetId) // fetch join
                .orElseThrow(RoomException.RoomNotFoundException::new);
        room.getParticipants().forEach(participant -> {
            if (participant.getMember().getId().equals(member.getId())) {
                throw new RoomException.AlreadyParticipatingRoom();
            }
        });
        room.getParticipants().add(CParticipant.builder()
                .member(member)
                .room(room)
                .isHost(false)
                .build());
        return roomCommandRepository.save(room);
    }

}
