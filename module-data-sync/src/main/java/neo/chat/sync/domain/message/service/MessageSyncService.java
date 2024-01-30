package neo.chat.sync.domain.message.service;

import neo.chat.persistence.command.entity.CMessage;

public interface MessageSyncService {

    void create(CMessage message);
    void update(CMessage before, CMessage after);
    void delete(CMessage message);

}
