package neo.chat.websocket.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.chat.jwt.service.TokenService;
import neo.chat.jwt.util.InvalidTokenException;
import neo.chat.persistence.command.ParticipantCommandRepository;
import neo.chat.persistence.query.MemberQueryRepository;
import neo.chat.persistence.query.RoomQueryRepository;
import neo.chat.persistence.query.config.QueryDBConfig;
import neo.chat.persistence.query.document.QMember;
import neo.chat.websocket.exception.ChatException;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreSendInterceptorSupport {

    private final TokenService tokenService;
    private final MemberQueryRepository memberQueryRepository;
    private final RoomQueryRepository roomQueryRepository;

    @Transactional(readOnly = true, transactionManager = QueryDBConfig.TRANSACTION_MANAGER)
    public void onConnect(StompHeaderAccessor accessor) {
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

    @Transactional(readOnly = true, transactionManager = QueryDBConfig.TRANSACTION_MANAGER)
    public void onSubscribe(StompHeaderAccessor accessor) {
        assert accessor.getDestination() != null;
        assert accessor.getDestination().startsWith("/sub/rooms/");
        assert accessor.getUser() != null;
        UUID roomId = UUID.fromString(accessor.getDestination().replace("/sub/rooms/", ""));
        UUID memberId = UUID.fromString(accessor.getUser().getName());
        if (!roomQueryRepository.checkSubscribable(roomId, memberId)) {
            throw new ChatException.InvalidAccessException();
        }
    }

}
