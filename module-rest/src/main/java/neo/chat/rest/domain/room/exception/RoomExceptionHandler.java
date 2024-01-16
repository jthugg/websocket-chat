package neo.chat.rest.domain.room.exception;

import neo.chat.rest.domain.room.controller.RoomController;
import neo.chat.rest.util.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = RoomController.class)
public class RoomExceptionHandler {

    @ExceptionHandler(RoomException.MemberNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleMemberNotFoundException(
            RoomException.MemberNotFoundException exception
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(RoomException.RoomNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleRoomNotFoundException(
            RoomException.RoomNotFoundException exception
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(RoomException.AlreadyParticipatingRoom.class)
    public ResponseEntity<BaseResponse<String>> handleAlreadyParticipatingRoom(
            RoomException.AlreadyParticipatingRoom exception
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

}
