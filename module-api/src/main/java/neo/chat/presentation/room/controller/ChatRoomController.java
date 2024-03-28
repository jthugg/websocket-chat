package neo.chat.presentation.room.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neo.chat.application.service.room.model.OpenChatRoomRequest;
import neo.chat.application.service.room.service.ChatRoomService;
import neo.chat.presentation.room.dto.response.ChatRoomDetails;
import neo.chat.presentation.util.Response;
import neo.chat.settings.route.ApiRoute;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping(ApiRoute.OPEN_CHAT_ROOM)
    public ResponseEntity<Response<ChatRoomDetails>> openChatRoom(@RequestBody @Valid OpenChatRoomRequest request) {
        return Response.responseEntityOf(
                HttpStatus.CREATED,
                ChatRoomDetails.from(chatRoomService.openChatRoom(request))
        );
    }

}
