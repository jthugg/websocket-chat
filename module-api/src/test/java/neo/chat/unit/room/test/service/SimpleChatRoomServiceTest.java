package neo.chat.unit.room.test.service;

import neo.chat.application.service.room.exception.AlreadyEnteredRoomException;
import neo.chat.application.service.room.exception.ChatRoomHasNoVacancyException;
import neo.chat.application.service.room.exception.ChatRoomPasswordNotMatchedException;
import neo.chat.application.service.room.exception.HostAuthorityRequiredException;
import neo.chat.application.service.room.exception.HostNotReplacedException;
import neo.chat.application.service.room.exception.ParticipantNotFountException;
import neo.chat.application.service.room.exception.RoomNotFoundException;
import neo.chat.application.service.room.model.EnterChatRoomRequest;
import neo.chat.application.service.room.model.OpenChatRoomRequest;
import neo.chat.application.service.room.service.SimpleChatRoomService;
import neo.chat.application.service.room.tx.ChatRoomTransactionScript;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.entity.participant.Participant;
import neo.chat.persistence.entity.room.Room;
import neo.chat.persistence.repository.room.RoomRepository;
import neo.chat.settings.context.AuthMemberContextHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("채팅방 서비스 레이어 테스트")
public class SimpleChatRoomServiceTest {

    @Mock
    ChatRoomTransactionScript chatRoomTransactionScript;
    @Spy
    PasswordEncoder passwordEncoder;
    @Mock
    RoomRepository roomRepository;
    @InjectMocks
    SimpleChatRoomService simpleChatRoomService;

    @Test
    @DisplayName("채팅 방 생성 테스트: 성공 케이스")
    void openTestCase01() {
        Member member = new Member(100L, "test", "test");
        OpenChatRoomRequest request = new OpenChatRoomRequest(
                "testTitle",
                "testPassword",
                10,
                "testNickname"
        );

        AuthMemberContextHolder.set(member);
        Mockito.when(chatRoomTransactionScript.createRoom(ArgumentMatchers.any())).thenReturn(null);

        simpleChatRoomService.openChatRoom(request);
    }

    @Test
    @DisplayName("채팅 방 입장 테스트: 성공 케이스")
    void enterTestCase01() {
        Member member = new Member(100L, "test", "test");
        EnterChatRoomRequest request = new EnterChatRoomRequest(12L, "iam00", null);
        Room room = Room.builder()
                .id(12L)
                .title("testTitle12")
                .capacity(50)
                .build();

        AuthMemberContextHolder.set(member);
        Mockito.when(roomRepository.findByIdJoinFetchParticipantsWithLock(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(room));

        Assertions.assertDoesNotThrow(() -> simpleChatRoomService.enterRoom(request));
    }

    @Test
    @DisplayName("채팅 방 입장 테스트: 실패 케이스 - 최대 인원 도달")
    void enterTestCase02() {
        Member member = new Member(100L, "test", "test");
        EnterChatRoomRequest request = new EnterChatRoomRequest(12L, "iam00", null);
        Room room = Room.builder()
                .id(12L)
                .title("testTitle12")
                .capacity(1)
                .build();

        AuthMemberContextHolder.set(member);
        Mockito.when(roomRepository.findByIdJoinFetchParticipantsWithLock(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(room));

        Assertions.assertThrows(ChatRoomHasNoVacancyException.class, () -> simpleChatRoomService.enterRoom(request));
    }

    @Test
    @DisplayName("채팅 방 입장 테스트: 실패 케이스 - 비밀번호 불일치")
    void enterTestCase03() {
        Member member = new Member(100L, "test", "test");
        EnterChatRoomRequest request = new EnterChatRoomRequest(12L, "iam00", null);
        Room room = Room.builder()
                .id(12L)
                .title("testTitle12")
                .capacity(50)
                .password("test")
                .build();

        AuthMemberContextHolder.set(member);
        Mockito.when(roomRepository.findByIdJoinFetchParticipantsWithLock(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(room));

        Assertions.assertThrows(
                ChatRoomPasswordNotMatchedException.class,
                () -> simpleChatRoomService.enterRoom(request)
        );
    }

    @Test
    @DisplayName("채팅 방 입장 테스트: 실패 케이스 - 이미 참여중인 방")
    void enterTestCase04() {
        Member member = new Member(100L, "test", "test");
        Room room = Room.builder()
                .id(12L)
                .title("test")
                .capacity(2)
                .build();
        room.getParticipants().add(Participant.builder()
                .id(100L)
                .isHost(false)
                .member(member)
                .room(room)
                .nickname("test")
                .build());
        EnterChatRoomRequest request = new EnterChatRoomRequest(12L, "iam00", null);

        AuthMemberContextHolder.set(member);
        Mockito.when(roomRepository.findByIdJoinFetchParticipantsWithLock(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(room));

        Assertions.assertThrows(
                AlreadyEnteredRoomException.class,
                () -> simpleChatRoomService.enterRoom(request)
        );
    }

    @Test
    @DisplayName("채팅 방 입장 테스트: 실패 케이스 - 존재하지 않는 방")
    void enterTestCase05() {
        Member member = new Member(100L, "test", "test");
        EnterChatRoomRequest request = new EnterChatRoomRequest(12L, "iam00", null);

        AuthMemberContextHolder.set(member);
        Mockito.when(roomRepository.findByIdJoinFetchParticipantsWithLock(ArgumentMatchers.anyLong()))
                .thenThrow(RoomNotFoundException.class);

        Assertions.assertThrows(
                RoomNotFoundException.class,
                () -> simpleChatRoomService.enterRoom(request)
        );
    }

    @Test
    @DisplayName("채팅 방 퇴장 테스트: 성공 케이스 - 채팅 방 참여자")
    void leaveTestCase01() {
        Member member = new Member(100L, "test", "test");
        AuthMemberContextHolder.set(member);
        Room room = Room.builder()
                .id(12L)
                .title("testTitle12")
                .capacity(2)
                .build();
        room.getParticipants().add(Participant.builder()
                .id(100L)
                .room(room)
                .member(member)
                .isHost(false)
                .nickname("iam100")
                .build());

        Mockito.when(roomRepository.findByIdJoinFetchParticipantsWithLock(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(room));

        Assertions.assertDoesNotThrow(() -> simpleChatRoomService.leaveRoom(12L));
        room.getParticipants().forEach(value -> {
            if (Objects.equals(value.getId(), member.getId())) {
                Assertions.assertTrue(value.isRemoved());
            }
        });
        Assertions.assertFalse(room.isRemoved());
    }

    @Test
    @DisplayName("채팅 방 퇴장 테스트: 성공 케이스 - 채팅 방 호스트")
    void leaveTestCase02() {
        Member member = new Member(100L, "test", "test");
        AuthMemberContextHolder.set(member);
        Room room = Room.builder()
                .id(12L)
                .title("testTitle12")
                .capacity(2)
                .build();
        room.getParticipants().add(Participant.builder()
                .id(100L)
                .room(room)
                .member(member)
                .isHost(true)
                .nickname("iam100")
                .build());

        Mockito.when(roomRepository.findByIdJoinFetchParticipantsWithLock(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(room));

        Assertions.assertDoesNotThrow(() -> simpleChatRoomService.leaveRoom(12L));
        room.getParticipants().forEach(value -> {
            if (Objects.equals(value.getId(), member.getId())) {
                Assertions.assertTrue(value.isRemoved());
            }
        });
        Assertions.assertTrue(room.isRemoved());
    }

    @Test
    @DisplayName("채팅 방 퇴장 테스트: 실패 케이스 - 다른 참여자가 있을 때 호스트가 퇴장하려는 경우")
    void leaveTestCase03() {
        Member member = new Member(100L, "test", "test");
        AuthMemberContextHolder.set(member);
        Room room = Room.builder()
                .id(12L)
                .title("testTitle12")
                .capacity(2)
                .build();
        room.getParticipants().add(Participant.builder()
                .id(100L)
                .room(room)
                .member(member)
                .isHost(true)
                .nickname("iam100")
                .build());
        room.setAttending(2);
        room.setSaturation();

        Mockito.when(roomRepository.findByIdJoinFetchParticipantsWithLock(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(room));

        Assertions.assertThrows(HostNotReplacedException.class, () -> simpleChatRoomService.leaveRoom(12L));
        room.getParticipants().forEach(value -> {
            if (Objects.equals(value.getId(), member.getId())) {
                Assertions.assertFalse(value.isRemoved());
            }
        });
        Assertions.assertFalse(room.isRemoved());
    }

    @Test
    @DisplayName("채팅 방 호스트 변경 테스트: 성공 케이스")
    void hostChangeTestCase01() {
        long hostMemberId = 100L;
        long targetMemberId = 101L;
        long roomId = 0L;
        long hostId = 0L;
        long targetId = 1L;
        Member hostMember = new Member(hostMemberId, "test", "test");
        Member targetMember = new Member(targetMemberId, "test", "test");
        Room room = Room.builder()
                .id(roomId)
                .capacity(10)
                .build();
        Participant host = Participant.builder()
                .id(hostId)
                .member(hostMember)
                .room(room)
                .isHost(true)
                .build();
        Participant target = Participant.builder()
                .id(targetId)
                .member(targetMember)
                .room(room)
                .isHost(false)
                .build();
        room.getParticipants().addAll(List.of(host, target));

        AuthMemberContextHolder.set(hostMember);
        Mockito.when(roomRepository.findByIdJoinFetchParticipantsWithLock(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(room));

        Assertions.assertDoesNotThrow(() -> simpleChatRoomService.changeHost(roomId, hostId, targetId));
        Assertions.assertFalse(host.getIsHost());
        Assertions.assertTrue(target.getIsHost());
    }

    @Test
    @DisplayName("채팅 방 호스트 변경 테스트: 실패 케이스 - 호스트가 아닌 사람이 요청 한 경우")
    void hostChangeTestCase02() {
        long nonHostMemberId = 100L;
        long targetMemberId = 101L;
        long roomId = 0L;
        long hostId = 0L;
        long targetId = 1L;
        Member hostMember = new Member(nonHostMemberId, "test", "test");
        Member targetMember = new Member(targetMemberId, "test", "test");
        Room room = Room.builder()
                .id(roomId)
                .capacity(10)
                .build();
        Participant NotAHost = Participant.builder()
                .id(hostId)
                .member(hostMember)
                .room(room)
                .isHost(false)
                .build();
        Participant target = Participant.builder()
                .id(targetId)
                .member(targetMember)
                .room(room)
                .isHost(false)
                .build();
        room.getParticipants().addAll(List.of(NotAHost, target));

        AuthMemberContextHolder.set(hostMember);
        Mockito.when(roomRepository.findByIdJoinFetchParticipantsWithLock(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(room));

        Assertions.assertThrows(
                HostAuthorityRequiredException.class,
                () -> simpleChatRoomService.changeHost(roomId, hostId, targetId)
        );
    }

    @Test
    @DisplayName("채팅 방 호스트 변경 테스트: 실패 케이스 - 채팅 방을 찾을 수 없는 경우")
    void hostChangeTestCase03() {
        long hostMemberId = 100L;
        long targetMemberId = 101L;
        long roomId = 0L;
        long hostId = 0L;
        long targetId = 1L;
        Member hostMember = new Member(hostMemberId, "test", "test");
        Member targetMember = new Member(targetMemberId, "test", "test");
        Room room = Room.builder()
                .id(roomId)
                .capacity(10)
                .build();
        Participant host = Participant.builder()
                .id(hostId)
                .member(hostMember)
                .room(room)
                .isHost(true)
                .build();
        Participant target = Participant.builder()
                .id(targetId)
                .member(targetMember)
                .room(room)
                .isHost(false)
                .build();
        room.getParticipants().addAll(List.of(host, target));

        AuthMemberContextHolder.set(hostMember);
        Mockito.when(roomRepository.findByIdJoinFetchParticipantsWithLock(ArgumentMatchers.anyLong()))
                .thenThrow(RoomNotFoundException.class);

        Assertions.assertThrows(
                RoomNotFoundException.class,
                () -> simpleChatRoomService.changeHost(roomId, hostId, targetId)
        );
    }

    @Test
    @DisplayName("채팅 방 호스트 변경 테스트: 실패 케이스 - 변경하려는 참여자가 없는 경우")
    void hostChangeTestCase04() {
        long hostMemberId = 100L;
        long roomId = 0L;
        long hostId = 0L;
        long targetId = 1L;
        Member hostMember = new Member(hostMemberId, "test", "test");
        Room room = Room.builder()
                .id(roomId)
                .capacity(10)
                .build();
        Participant host = Participant.builder()
                .id(hostId)
                .member(hostMember)
                .room(room)
                .isHost(true)
                .build();
        room.getParticipants().add(host);

        AuthMemberContextHolder.set(hostMember);
        Mockito.when(roomRepository.findByIdJoinFetchParticipantsWithLock(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(room));

        Assertions.assertThrows(
                ParticipantNotFountException.class,
                () -> simpleChatRoomService.changeHost(roomId, hostId, targetId)
        );
    }

}
