package neo.chat.presentation.room.advice;

import jakarta.validation.ConstraintViolationException;
import neo.chat.application.service.exception.ApplicationException;
import neo.chat.presentation.room.controller.ChatRoomController;
import neo.chat.presentation.util.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = ChatRoomController.class)
public class ChatRoomControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Response<String>> handleApplicationException(ApplicationException exception) {
        return Response.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response<String>> handleConstraintViolationException(ConstraintViolationException exception) {
        return Response.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        return Response.responseEntityOf(
                HttpStatus.BAD_REQUEST,
                exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        );
    }

}
