package neo.chat.unit.room.test.service;

import neo.chat.application.service.room.model.OpenChatRoomRequest;
import neo.chat.application.service.room.service.SimpleChatRoomService;
import neo.chat.application.service.room.tx.ChatRoomTransactionScript;
import neo.chat.persistence.entity.member.Member;
import neo.chat.settings.context.AuthMemberContextHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("채팅방 서비스 레이어 테스트")
public class SimpleChatRoomServiceTest {

    @Mock
    ChatRoomTransactionScript chatRoomTransactionScript;
    @Mock
    PasswordEncoder passwordEncoder;
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

}
