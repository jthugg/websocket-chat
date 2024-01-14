package neo.chat.security.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import neo.chat.jwt.service.TokenService;
import neo.chat.jwt.util.InvalidTokenException;
import neo.chat.jwt.util.TokenConstant;
import neo.chat.security.service.SecurityUserDetailsService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    public static final String INVALID_TOKEN_MESSAGE = "사용할 수 없는 토큰입니다.";

    private final TokenService tokenService;
    private final SecurityUserDetailsService securityUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).ifPresentOrElse(token -> {
                Assert.isTrue(
                        token.startsWith(TokenConstant.BEARER)
                                && !tokenService.isExpired(token.substring(TokenConstant.BEARER.length())),
                        INVALID_TOKEN_MESSAGE
                );
                UserDetails user = securityUserDetailsService
                        .loadUserByUsername(tokenService.getUsername(token.substring(TokenConstant.BEARER.length())));
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        user.getAuthorities()
                ));
            }, SecurityContextHolder::clearContext);
        } catch (InvalidTokenException
                | IllegalArgumentException
                | UsernameNotFoundException exception) {
            SecurityContextHolder.clearContext();
        } finally {
            filterChain.doFilter(request, response);
        }
    }

}
