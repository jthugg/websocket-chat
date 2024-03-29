package neo.chat.rest.domain.room.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neo.chat.rest.domain.room.dto.request.Create;
import neo.chat.rest.domain.room.dto.request.Enter;
import neo.chat.rest.domain.room.dto.request.Search;
import neo.chat.rest.domain.room.dto.request.Update;
import neo.chat.rest.domain.room.dto.response.Room;
import neo.chat.rest.domain.room.dto.response.SearchResult;
import neo.chat.rest.domain.room.service.RoomSearchService;
import neo.chat.rest.domain.room.service.RoomService;
import neo.chat.rest.util.ApiRoute;
import neo.chat.rest.util.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomSearchService roomSearchService;
    private final RoomService roomService;

    @GetMapping(ApiRoute.ROOM_SEARCH)
    public ResponseEntity<BaseResponse<SearchResult>> searchRoom(Search dto) {
        return BaseResponse.responseEntityOf(HttpStatus.OK, SearchResult.from(roomSearchService.search(dto)));
    }

    @PostMapping(ApiRoute.ROOM_CREATE)
    public ResponseEntity<BaseResponse<Room>> createRoom(@RequestBody @Valid Create dto) {
        return BaseResponse.responseEntityOf(HttpStatus.OK, Room.asDetail(roomService.create(dto)));
    }

    @PostMapping(ApiRoute.ROOM_ENTER)
    public ResponseEntity<BaseResponse<Room>> enterRoom(
            @PathVariable UUID targetRoomId,
            @RequestBody @Valid Enter dto
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.OK, Room.asDetail(roomService.enter(targetRoomId, dto)));
    }

    @DeleteMapping(ApiRoute.ROOM_LEAVE)
    public ResponseEntity<BaseResponse<Void>> leaveRoom(@PathVariable UUID targetRoomId) {
        roomService.leave(targetRoomId);
        return BaseResponse.voidResponseEntityOf(HttpStatus.OK);
    }

    @PutMapping(ApiRoute.ROOM_UPDATE)
    public ResponseEntity<BaseResponse<Room>> updateRoomData(
            @PathVariable UUID targetRoomId,
            @RequestBody @Valid Update dto
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.OK, Room.asDetail(roomService.update(targetRoomId, dto)));
    }

    @DeleteMapping(ApiRoute.ROOM_DELETE)
    public ResponseEntity<BaseResponse<Void>> deleteRoom(@PathVariable UUID targetRoomId) {
        roomService.delete(targetRoomId);
        return BaseResponse.voidResponseEntityOf(HttpStatus.OK);
    }

}
