package neo.chat.sync.domain.message.service;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.command.entity.CMessage;
import neo.chat.persistence.query.MessageQueryRepository;
import neo.chat.persistence.query.config.QueryDBConfig;
import neo.chat.persistence.query.document.QMessage;
import neo.chat.sync.proxy.QMemberProxy;
import neo.chat.sync.proxy.QRoomProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SimpleMessageSyncService implements MessageSyncService {

    private final MessageQueryRepository messageQueryRepository;

    @Override
    @Transactional(transactionManager = QueryDBConfig.TRANSACTION_MANAGER)
    public void create(CMessage message) {
        messageQueryRepository.save(new QMessage(
                message.getId(),
                message.getCreatedAt(),
                new QMemberProxy(message.getSender().getId()),
                new QRoomProxy(message.getRoom().getId()),
                message.getContent()
        ));
    }

    @Override
    @Transactional(transactionManager = QueryDBConfig.TRANSACTION_MANAGER)
    public void update(CMessage before, CMessage after) {
        // not support. do nothing
    }

    @Override
    @Transactional(transactionManager = QueryDBConfig.TRANSACTION_MANAGER)
    public void delete(CMessage message) {
        assert message.getId() != null;
        messageQueryRepository.deleteById(message.getId());
    }

}
