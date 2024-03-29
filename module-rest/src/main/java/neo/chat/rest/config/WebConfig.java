package neo.chat.rest.config;

import neo.chat.rest.domain.room.dto.RoomSearchHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String MAPPING_PATTERN = "/**";
    public static final String[] ALLOWED_ORIGIN_PATTERNS = { "*" };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(MAPPING_PATTERN)
                .allowedOriginPatterns(ALLOWED_ORIGIN_PATTERNS)
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.SET_COOKIE, HttpHeaders.CONTENT_TYPE)
                .allowedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.SET_COOKIE, HttpHeaders.CONTENT_TYPE)
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name()
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RoomSearchHandlerMethodArgumentResolver());
    }
}
