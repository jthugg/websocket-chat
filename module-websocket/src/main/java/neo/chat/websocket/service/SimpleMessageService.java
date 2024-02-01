package neo.chat.websocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.chat.persistence.command.MessageCommandRepository;
import neo.chat.persistence.command.ParticipantCommandRepository;
import neo.chat.persistence.command.config.CommandDBConfig;
import neo.chat.persistence.command.entity.CMessage;
import neo.chat.persistence.command.entity.CParticipant;
import neo.chat.websocket.exception.ChatException;
import neo.chat.websocket.model.ChatMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleMessageService implements MessageService {

    private final MessageCommandRepository messageCommandRepository;
    private final ParticipantCommandRepository participantCommandRepository;
    private final SimpMessagingTemplate template;

    @Override
    @Transactional(transactionManager = CommandDBConfig.TRANSACTION_MANAGER)
    public void create(UUID roomId, UUID memberId, String payload) {
        CParticipant participant = participantCommandRepository
                .findByRoomIdAndMemberId(memberId, roomId)
                .orElseThrow(ChatException.InvalidAccessException::new);
        messageCommandRepository.save(CMessage.builder()
                .room(participant.getRoom())
                .sender(participant.getMember())
                .content(payload)
                .build());
    }

    @Override
    public void publish(CMessage message) {
        template.convertAndSend(
                "/sub/rooms/" + message.getRoom().getId().toString(),
                new ChatMessage(
                        message.getRoom().getId(),
                        message.getSender().getId(),
                        message.getContent(),
                        message.getCreatedAt()
                )
        );
    }

}
