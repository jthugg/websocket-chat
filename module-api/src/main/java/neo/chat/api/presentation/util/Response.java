package neo.chat.api.presentation.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.function.Consumer;

@Getter
public class Response<T> {

    private final String requestId;
    private final Instant timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T content;

    private Response() {
        this.requestId = MDC.get(RequestTracingFilter.REQUEST_ID);
        this.timestamp = Instant.now();
        this.content = null;
    }

    private Response(T content) {
        this.requestId = MDC.get(RequestTracingFilter.REQUEST_ID);
        this.timestamp = Instant.now();
        this.content = content;
    }

    public static ResponseEntity<Response<Void>> voidResponseEntityOf(HttpStatus status) {
        return ResponseEntity.status(status).body(new Response<>());
    }

    public static ResponseEntity<Response<Void>> headedVoidResponseEntityOf(
            HttpStatus status,
            Consumer<HttpHeaders> headersConsumer
    ) {
        return ResponseEntity.status(status).headers(headersConsumer).body(new Response<>());
    }

    public static <T> ResponseEntity<Response<T>> responseEntityOf(HttpStatus status, T content) {
        return ResponseEntity.status(status).body(new Response<>(content));
    }

    public static <T> ResponseEntity<Response<T>> headedResponseEntityOf(
            HttpStatus status,
            Consumer<HttpHeaders> headersConsumer,
            T content
    ) {
        return ResponseEntity.status(status).headers(headersConsumer).body(new Response<>(content));
    }

}
