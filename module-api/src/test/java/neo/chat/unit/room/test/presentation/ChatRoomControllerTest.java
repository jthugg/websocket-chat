package neo.chat.unit.room.test.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import neo.chat.application.service.auth.exception.InvalidTokenException;
import neo.chat.application.service.auth.service.MemberAuthService;
import neo.chat.application.service.room.model.OpenChatRoomRequest;
import neo.chat.application.service.room.service.ChatRoomService;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.entity.participant.Participant;
import neo.chat.persistence.entity.room.Room;
import neo.chat.presentation.room.controller.ChatRoomController;
import neo.chat.settings.route.ApiRoute;
import neo.chat.settings.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

@Import(SecurityConfig.class)
@WebMvcTest(ChatRoomController.class)
@DisplayName("채팅 방 프레젠테이션 레이어 테스트")
public class ChatRoomControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    ChatRoomService chatRoomService;
    @MockBean
    MemberAuthService memberAuthService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("채팅 방 생성 테스트: 성공 케이스")
    void openChatRoomTestCase01() throws Exception {
        Member member = new Member(0L, "test", "test");
        OpenChatRoomRequest request = new OpenChatRoomRequest("welcome to test room :) 🚀", 3);
        Room room = Room.builder()
                .id(0L)
                .title(request.title())
                .capacity(request.capacity())
                .build();
        room.getParticipants().add(Participant.builder()
                .id(0L)
                .member(member)
                .room(room)
                .isHost(true)
                .nickname(member.getUsername())
                .build());
        String requestBody = objectMapper.writeValueAsString(request);

        Mockito.when(chatRoomService.openChatRoom(ArgumentMatchers.any()))
                .thenReturn(room);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.OPEN_CHAT_ROOM)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    @DisplayName("채팅 방 생성 테스트: 실패 케이스 - 너무 긴 방 제목")
    void openChatRoomTestCase02() throws Exception {
        Map<String, Object> request = Map.of(
                "title", "welcome to test room :) 🚀🚀🚀🚀🚀🚀🚀🚀🚀🚀🚀",
                "capacity", 3
        );
        String requestBody = objectMapper.writeValueAsString(request);

        Mockito.when(chatRoomService.openChatRoom(ArgumentMatchers.any()))
                .thenThrow(ConstraintViolationException.class);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.OPEN_CHAT_ROOM)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    @DisplayName("채팅 방 생성 테스트: 실패 케이스 - 방 제목 누락")
    void openChatRoomTestCase03() throws Exception {
        Map<String, Object> request = Map.of("capacity", 3);
        String requestBody = objectMapper.writeValueAsString(request);

        Mockito.when(chatRoomService.openChatRoom(ArgumentMatchers.any()))
                .thenThrow(ConstraintViolationException.class);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.OPEN_CHAT_ROOM)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    @DisplayName("채팅 방 생성 테스트: 실패 케이스 - 비밀번호 제약사항 위반")
    void openChatRoomTestCase04() throws Exception {
        Map<String, Object> request = Map.of(
                "title", "welcome to test room :) 🚀",
                "password", "",
                "capacity", 3
        );
        String requestBody = objectMapper.writeValueAsString(request);

        Mockito.when(chatRoomService.openChatRoom(ArgumentMatchers.any()))
                .thenThrow(ConstraintViolationException.class);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.OPEN_CHAT_ROOM)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    @DisplayName("채팅 방 생성 테스트: 실패 케이스 - 최대 인원 제약사항 위반")
    void openChatRoomTestCase05() throws Exception {
        Map<String, Object> request = Map.of(
                "title", "welcome to test room :) 🚀",
                "capacity", 1
        );
        String requestBody = objectMapper.writeValueAsString(request);

        Mockito.when(chatRoomService.openChatRoom(ArgumentMatchers.any()))
                .thenThrow(ConstraintViolationException.class);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.OPEN_CHAT_ROOM)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    @DisplayName("채팅 방 생성 테스트: 실패 케이스 - 닉네임 제약사항 위반")
    void openChatRoomTestCase06() throws Exception {
        Map<String, Object> request = Map.of(
                "title", "welcome to test room :) 🚀",
                "capacity", 3,
                "nickname", "tttteeeessssttttnnnniiiicccckkkknnnnaaaammmmeeee"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        Mockito.when(chatRoomService.openChatRoom(ArgumentMatchers.any()))
                .thenThrow(ConstraintViolationException.class);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.OPEN_CHAT_ROOM)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("채팅 방 생성 테스트: 실패 케이스 - 인증되지 않은 사용자 요청")
    void openChatRoomTestCase07() throws Exception {
        Map<String, Object> request = Map.of(
                "title", "welcome to test room :) 🚀",
                "capacity", 3,
                "nickname", "testNickname"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        Mockito.when(chatRoomService.openChatRoom(ArgumentMatchers.any()))
                .thenThrow(ConstraintViolationException.class);

        mvc.perform(MockMvcRequestBuilders.post(ApiRoute.OPEN_CHAT_ROOM)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

}
