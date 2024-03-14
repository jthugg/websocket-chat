package neo.chat.application.service.room.service;

import lombok.RequiredArgsConstructor;
import neo.chat.application.service.room.model.OpenChatRoomRequest;
import neo.chat.application.service.room.tx.ChatRoomTransactionScript;
import neo.chat.application.util.EntityIdGenerator;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.entity.participant.Participant;
import neo.chat.persistence.entity.room.Room;
import neo.chat.settings.context.AuthMemberContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SimpleChatRoomService implements ChatRoomService {

    private final ChatRoomTransactionScript transactionScript;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Room openChatRoom(OpenChatRoomRequest request) {
        String password = null;
        if (request.hasPassword()) {
            password = passwordEncoder.encode(request.password());
        }
        Member member = AuthMemberContextHolder.get();
        Room room = Room.builder()
                .id(EntityIdGenerator.CHAT_ROOM.getIdGenerator().generate().toLong())
                .title(request.title())
                .password(password)
                .capacity(request.capacity())
                .build();
        room.getParticipants().add(Participant.builder()
                .id(EntityIdGenerator.PARTICIPANT.getIdGenerator().generate().toLong())
                .member(member)
                .room(room)
                .isHost(true)
                .nickname(Objects.requireNonNullElse(request.nickname(), member.getUsername()))
                .build());
        return transactionScript.createRoom(room);
    }

}
