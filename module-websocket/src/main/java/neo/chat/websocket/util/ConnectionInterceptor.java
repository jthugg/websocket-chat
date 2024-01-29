package neo.chat.websocket.util;

import lombok.RequiredArgsConstructor;
import neo.chat.jwt.service.TokenService;
import neo.chat.jwt.util.InvalidTokenException;
import neo.chat.persistence.query.MemberQueryRepository;
import neo.chat.persistence.query.document.QMember;
import neo.chat.websocket.exception.ChatException;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class ConnectionInterceptor implements ChannelInterceptor {

    private final TokenService tokenService;
    private final MemberQueryRepository memberQueryRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String accessToken = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
            if (tokenService.isExpired(accessToken)) {
                throw new InvalidTokenException();
            }
            String username = tokenService.getUsername(accessToken);
            QMember member = memberQueryRepository.findByUsername(username)
                    .orElseThrow(ChatException.InvalidUsernameException::new);
            Principal chatUser = new ChatUser(member);
            accessor.setUser(chatUser);
        }
        return message;
    }

}
