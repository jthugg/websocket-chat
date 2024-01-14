package neo.chat.rest.domain.member.exception;

import neo.chat.rest.domain.member.controller.MemberController;
import neo.chat.rest.util.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = MemberController.class)
public class MemberExceptionHandler {

    @ExceptionHandler(MemberException.LoginUsernameNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleLoginUsernameNotFoundException(
            MemberException.LoginUsernameNotFoundException exception
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MemberException.PasswordNotMatchedException.class)
    public ResponseEntity<BaseResponse<String>> handlePasswordNotMatchedException(
            MemberException.PasswordNotMatchedException exception
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

}
