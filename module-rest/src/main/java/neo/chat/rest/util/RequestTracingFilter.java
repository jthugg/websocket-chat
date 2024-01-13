package neo.chat.rest.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class RequestTracingFilter extends GenericFilterBean {

    public static final String REQUEST_ID = "requestId";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        MDC.put(REQUEST_ID, UUID.randomUUID().toString());
        chain.doFilter(request, response);
    }

}
