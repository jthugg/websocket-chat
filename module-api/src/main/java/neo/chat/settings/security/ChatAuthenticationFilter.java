package neo.chat.settings.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import neo.chat.application.service.auth.exception.InvalidTokenException;
import neo.chat.application.service.auth.exception.MemberNotFoundException;
import neo.chat.application.service.auth.properties.JWTProperties;
import neo.chat.application.service.auth.service.MemberAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class ChatAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_PREFIX_NOT_SET_MESSAGE = "인증 토큰 접두어가 없습니다.";

    private final MemberAuthService authService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).ifPresent(header -> {
                assert header.startsWith(JWTProperties.PREFIX): TOKEN_PREFIX_NOT_SET_MESSAGE;
                UserDetails userDetails = authService.authorization(header.substring(JWTProperties.PREFIX.length()));
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                ));
            });
        } catch (AssertionError
                | InvalidTokenException
                | MemberNotFoundException exception) {
            SecurityContextHolder.clearContext();
        } finally {
            filterChain.doFilter(request, response);
        }
    }

}
