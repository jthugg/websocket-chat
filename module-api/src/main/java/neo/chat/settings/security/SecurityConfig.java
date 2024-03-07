package neo.chat.settings.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import neo.chat.settings.route.ApiRoute;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper mapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(ApiRoute.PERMIT_ALL).permitAll()
                        .requestMatchers(ApiRoute.ANONYMOUS).anonymous()
                        .requestMatchers(ApiRoute.AUTHENTICATED).authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(
                        new ChatAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler(new ChatAccessDeniedHandler(mapper))
                        .authenticationEntryPoint(new ChatAuthenticationEntryPoint(mapper)))
                .build();
    }

}
