package neo.chat.unit.room.test.tx;

import neo.chat.application.service.room.tx.ChatRoomTransactionScript;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.entity.participant.Participant;
import neo.chat.persistence.entity.room.Room;
import neo.chat.persistence.repository.room.RoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("채팅 방 트랜잭션 스크립트 테스트")
public class ChatRoomTransactionScriptTest {

    @Mock
    RoomRepository roomRepository;
    @InjectMocks
    ChatRoomTransactionScript chatRoomTransactionScript;

    @Test
    @DisplayName("채팅 방 레코드 생성")
    void createChatRoomTestCase01() {
        long roomId = 100L;
        String title = "testTitle";
        String password = "testPassword";
        int capacity = 3;
        long participantId = 100L;
        String nickname = "testNickname";

        Member member = new Member(100L, "test", "test");
        Room room = Room.builder()
                .id(roomId)
                .title(title)
                .password(password)
                .capacity(capacity)
                .build();
        room.getParticipants().add(Participant.builder()
                .id(participantId)
                .member(member)
                .room(room)
                .isHost(true)
                .nickname(nickname)
                .build());

        Mockito.when(roomRepository.save(ArgumentMatchers.any())).thenReturn(room);

        Assertions.assertDoesNotThrow(() -> chatRoomTransactionScript.createRoom(room));
    }

}
