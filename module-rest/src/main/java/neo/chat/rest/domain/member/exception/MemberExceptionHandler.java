package neo.chat.rest.domain.member.exception;

import neo.chat.jwt.util.InvalidTokenException;
import neo.chat.jwt.util.TokenConstant;
import neo.chat.rest.domain.member.controller.MemberController;
import neo.chat.rest.util.BaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
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

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidTokenException(InvalidTokenException exception) {
        // 토큰 재발급 상황에서 발생하는 예외
        return BaseResponse.headedResponseEntityOf(HttpStatus.BAD_REQUEST, headers -> {
            headers.add(
                    HttpHeaders.SET_COOKIE,
                    ResponseCookie.from(TokenConstant.REFRESH_TOKEN).maxAge(0).build().toString()
            );
        }, exception.getMessage());
    }

    @ExceptionHandler(MemberException.UsernameNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleUsernameNotFoundException(
            MemberException.UsernameNotFoundException exception
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<BaseResponse<String>> handleMissingRequestCookieException(
            MissingRequestCookieException exception
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

}
