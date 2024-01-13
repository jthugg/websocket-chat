package neo.chat.rest.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.function.Consumer;

@Getter
public class BaseResponse<T> {

    private final String requestId;
    private final Instant timestamp;
    private final HttpStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T content;

    private BaseResponse(HttpStatus status) {
        this.requestId = MDC.get(RequestTracingFilter.REQUEST_ID);
        this.timestamp = Instant.now();
        this.status = status;
        this.content = null;
    }

    private BaseResponse(HttpStatus status, T content) {
        this.requestId = MDC.get(RequestTracingFilter.REQUEST_ID);
        this.timestamp = Instant.now();
        this.status = status;
        this.content = content;
    }

    public static <T> ResponseEntity<BaseResponse<T>> responseEntityOf(HttpStatus status, T content) {
        return ResponseEntity.status(status).body(new BaseResponse<>(status, content));
    }

    public static ResponseEntity<BaseResponse<Void>> voidResponseEntityOf(HttpStatus status) {
        return ResponseEntity.status(status).body(new BaseResponse<>(status));
    }

    public static <T> ResponseEntity<BaseResponse<T>> headedResponseEntityOf(
            HttpStatus status,
            Consumer<HttpHeaders> headersConsumer,
            T content
    ) {
        return ResponseEntity.status(status).headers(headersConsumer).body(new BaseResponse<>(status, content));
    }

    public static ResponseEntity<BaseResponse<Void>> headedVoidResponseEntityOf(
            HttpStatus status,
            Consumer<HttpHeaders> headersConsumer
    ) {
        return ResponseEntity.status(status).headers(headersConsumer).body(new BaseResponse<>(status));
    }

}
