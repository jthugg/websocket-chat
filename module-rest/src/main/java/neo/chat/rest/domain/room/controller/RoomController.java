package neo.chat.rest.domain.room.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neo.chat.rest.domain.room.dto.request.Create;
import neo.chat.rest.domain.room.dto.response.Room;
import neo.chat.rest.domain.room.service.RoomService;
import neo.chat.rest.util.ApiRoute;
import neo.chat.rest.util.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping(ApiRoute.ROOM_CREATE)
    public ResponseEntity<BaseResponse<Room>> createRoom(@RequestBody @Valid Create dto) {
        return BaseResponse.responseEntityOf(HttpStatus.OK, Room.from(roomService.create(dto)));
    }

}
