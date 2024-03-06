package neo.chat.api.settings.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import neo.chat.api.application.auth.service.TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class ChatAuthenticationFilter extends OncePerRequestFilter {

    private static final String INVALID_TOKEN_MESSAGE = "사용할 수 없는 토큰입니다.";

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).ifPresent(token -> {
            try {
                DecodedJWT decodedJWT = tokenService.verify(token.substring(TokenService.TOKEN_PREFIX.length()));
                assert TokenService.ACCESS_TOKEN_SUBJECT.equals(decodedJWT.getSubject()): INVALID_TOKEN_MESSAGE;
                String userId = decodedJWT.getClaim(TokenService.USER_ID_CLAIM).asString();
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                ));
            } catch (IndexOutOfBoundsException |
                     JWTVerificationException |
                     AssertionError |
                     UsernameNotFoundException exception) {
                SecurityContextHolder.clearContext();
            }
        });
        filterChain.doFilter(request, response);
    }

}
