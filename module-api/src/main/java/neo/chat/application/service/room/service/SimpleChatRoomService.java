package neo.chat.application.service.room.service;

import lombok.RequiredArgsConstructor;
import neo.chat.application.service.room.exception.AlreadyEnteredRoomException;
import neo.chat.application.service.room.exception.ChatRoomHasNoVacancyException;
import neo.chat.application.service.room.exception.ChatRoomPasswordNotMatchedException;
import neo.chat.application.service.room.exception.HostAuthorityRequiredException;
import neo.chat.application.service.room.exception.HostNotReplacedException;
import neo.chat.application.service.room.exception.ParticipantNotFountException;
import neo.chat.application.service.room.exception.RoomNotFoundException;
import neo.chat.application.service.room.model.ChangeChatRoomInfoRequest;
import neo.chat.application.service.room.model.ChatRoomSortOption;
import neo.chat.application.service.room.model.EnterChatRoomRequest;
import neo.chat.application.service.room.model.OpenChatRoomRequest;
import neo.chat.application.service.room.model.SearchChatRoomRequest;
import neo.chat.application.service.room.tx.ChatRoomTransactionScript;
import neo.chat.application.util.EntityIdGenerator;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.entity.participant.Participant;
import neo.chat.persistence.entity.room.Room;
import neo.chat.persistence.repository.room.RoomRepository;
import neo.chat.persistence.repository.room.RoomSearchRepository;
import neo.chat.settings.context.AuthMemberContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SimpleChatRoomService implements ChatRoomService {

    private final ChatRoomTransactionScript transactionScript;
    private final PasswordEncoder passwordEncoder;
    private final RoomSearchRepository roomSearchRepository;
    private final RoomRepository roomRepository;

    @Override
    public Room openChatRoom(OpenChatRoomRequest request) {
        String password = null;
        if (request.hasPassword()) {
            password = passwordEncoder.encode(request.password());
        }
        Member member = AuthMemberContextHolder.get();
        Room room = Room.builder()
                .id(EntityIdGenerator.CHAT_ROOM.getIdGenerator().generate().toLong())
                .title(request.title())
                .password(password)
                .capacity(request.capacity())
                .build();
        room.getParticipants().add(Participant.builder()
                .id(EntityIdGenerator.PARTICIPANT.getIdGenerator().generate().toLong())
                .member(member)
                .room(room)
                .isHost(true)
                .nickname(Objects.requireNonNullElse(request.nickname(), member.getUsername()))
                .build());
        return transactionScript.createRoom(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> searchChatRoom(SearchChatRoomRequest request) {
        return roomSearchRepository.searchRoom(request);
    }

    @Override
    public List<Participant> getParticipatingRooms(Long cursorId, int fetchSize) {
        Member member = AuthMemberContextHolder.get();
        int defaultPageNumber = 0;
        Pageable pageable = PageRequest.of(
                defaultPageNumber,
                fetchSize,
                Sort.Direction.DESC,
                ChatRoomSortOption.DEFAULT.getFieldName()
        );
        if (cursorId == null) {
            return transactionScript.getParticipatingRooms(member, pageable);
        }
        return transactionScript.getParticipatingRooms(member, cursorId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Room getRoomData(Long id) {
        return roomRepository.findByIdAndRemovedAtIsNull(id).orElseThrow(RoomNotFoundException::new);
    }

    @Override
    @Transactional
    public Room enterRoom(EnterChatRoomRequest request) {
        Member member = AuthMemberContextHolder.get();
        Room room = roomRepository.findByIdJoinFetchParticipantsWithLock(request.roomId())
                .orElseThrow(RoomNotFoundException::new);

        room.getParticipants().forEach(participant -> {
            if (Objects.equals(participant.getMember().getId(), member.getId())) {
                throw new AlreadyEnteredRoomException();
            }
        });

        if (room.isPublicRoom() || passwordEncoder.matches(request.password(), room.getPassword())) {
            if (room.getAttending() >= room.getCapacity()) {
                throw new ChatRoomHasNoVacancyException();
            }
            room.getParticipants().add(new Participant(
                    EntityIdGenerator.PARTICIPANT.getIdGenerator().generate().toLong(),
                    member,
                    room,
                    false,
                    request.nickname()
            ));
            room.setAttending(room.getParticipants().size());
            room.setSaturation();
            return room;
        }

        throw new ChatRoomPasswordNotMatchedException();
    }

    @Override
    @Transactional
    public void leaveRoom(Long id) {
        Room room = roomRepository.findByIdJoinFetchParticipantsWithLock(id).orElseThrow(RoomNotFoundException::new);
        Member member = AuthMemberContextHolder.get();
        room.getParticipants().forEach(participant -> {
            if (!Objects.equals(participant.getMember().getId(), member.getId())) {
                return;
            }
            if (!participant.getIsHost()) {
                participant.remove();
                room.setAttending(room.getAttending() - 1);
                room.setSaturation();
                return;
            }
            if (room.getAttending() > 1) {
                throw new HostNotReplacedException();
            }
            participant.remove();
            room.setAttending(0);
            room.setSaturation();
            room.remove();
        });
    }

    @Override
    @Transactional
    public List<Participant> changeHost(Long roomId, Long myParticipantId, Long targetParticipantId) {
        Room room = roomRepository.findByIdJoinFetchParticipantsWithLock(roomId)
                .orElseThrow(RoomNotFoundException::new);
        Member requester = AuthMemberContextHolder.get();
        Participant host = null;
        Participant target = null;
        for (Participant participant : room.getParticipants()) {
            if (host != null && target != null) {
                break;
            }
            if (participant.getIsHost() && Objects.equals(participant.getMember().getId(), requester.getId())) {
                host = participant;
            }
            if (Objects.equals(participant.getId(), targetParticipantId)) {
                target = participant;
            }
        }
        if (host == null) {
            throw new HostAuthorityRequiredException();
        }
        if (target == null) {
            throw new ParticipantNotFountException();
        }
        host.changeHost(target);
        return room.getParticipants();
    }

    @Override
    @Transactional
    public Room changeRoomInfo(ChangeChatRoomInfoRequest request) {
        Member member = AuthMemberContextHolder.get();
        Room room = roomRepository.findRoomDataWithLockForUpdate(request.id(), member.getId())
                .orElseThrow(RoomNotFoundException::new);
        room.setTitle(request.title());
        room.setPassword(request.password());
        room.setCapacity(request.capacity());
        room.setSaturation();
        return room;
    }

}
