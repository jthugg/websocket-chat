package neo.chat.websocket.util;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ConnectionInterceptor implements ChannelInterceptor {

    private final PreSendInterceptorSupport preSendInterceptorSupport;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        assert accessor.getCommand() != null;
        switch (accessor.getCommand()) {
            case CONNECT -> preSendInterceptorSupport.onConnect(accessor);
            case SUBSCRIBE -> preSendInterceptorSupport.onSubscribe(accessor);
        }
        return message;
    }

}
