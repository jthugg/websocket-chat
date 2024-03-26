package neo.chat.unit.room.test.service;

import neo.chat.application.service.room.exception.AlreadyEnteredRoomException;
import neo.chat.application.service.room.exception.ChatRoomHasNoVacancyException;
import neo.chat.application.service.room.exception.ChatRoomPasswordNotMatchedException;
import neo.chat.application.service.room.model.EnterChatRoomRequest;
import neo.chat.application.service.room.model.OpenChatRoomRequest;
import neo.chat.application.service.room.service.SimpleChatRoomService;
import neo.chat.application.service.room.tx.ChatRoomTransactionScript;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.entity.room.Room;
import neo.chat.persistence.repository.participant.ParticipantRepository;
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

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("채팅방 서비스 레이어 테스트")
public class SimpleChatRoomServiceTest {

    @Mock
    ChatRoomTransactionScript chatRoomTransactionScript;
    @Spy
    PasswordEncoder passwordEncoder;
    @Mock
    ParticipantRepository participantRepository;
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
        Mockito.when(participantRepository.existsByMemberAndRoomIdAndRemovedAtIsNull(
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        )).thenReturn(false);
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
        Mockito.when(participantRepository.existsByMemberAndRoomIdAndRemovedAtIsNull(
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        )).thenReturn(false);
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
        Mockito.when(participantRepository.existsByMemberAndRoomIdAndRemovedAtIsNull(
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        )).thenReturn(false);
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
        EnterChatRoomRequest request = new EnterChatRoomRequest(12L, "iam00", null);

        AuthMemberContextHolder.set(member);
        Mockito.when(participantRepository.existsByMemberAndRoomIdAndRemovedAtIsNull(
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        )).thenReturn(true);

        Assertions.assertThrows(
                AlreadyEnteredRoomException.class,
                () -> simpleChatRoomService.enterRoom(request)
        );
    }

}
