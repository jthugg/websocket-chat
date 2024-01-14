package neo.chat.rest.domain.member.exception;

import jakarta.validation.ConstraintViolationException;
import neo.chat.rest.domain.member.controller.MemberController;
import neo.chat.rest.util.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice(basePackageClasses = MemberController.class)
public class MemberExceptionHandler {

    /**
     * '@Validated' 애노테이션이 달린 클래스에서 메서드 인자에 '@Pattern과' 같은 인자를 직접 검증하는 애노테이션 조건을 만족하지 못할 때
     *
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<String>> handleConstraintViolationException(
            ConstraintViolationException exception
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * 메서드 인자에 '@Valid' 애노테이션이 붙고 그 내부 필드 값이 검증 애노테이션의 조건을 만족하지 못할 때
     *
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        return BaseResponse.responseEntityOf(
                HttpStatus.BAD_REQUEST,
                exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        );
    }

    /**
     * 필요한 쿼리파라미터가 없을 때
     *
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<String>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * SQL 제약사항 위반할 때<br/>
     * ex) foreign key, unique key, primary key, not null or more...
     *
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<BaseResponse<String>> handleSQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException exception
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * HTTP 메세지를 읽을 수 없을 때<br/>
     * ex) 필요한 request body가 없을 때 or more...
     *
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<String>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {
        return BaseResponse.responseEntityOf(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

}
