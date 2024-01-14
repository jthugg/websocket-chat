package neo.chat.sync.domain.member.service;

import neo.chat.persistence.command.entity.CMember;

public interface MemberSyncService {

    void create(CMember member);
    void update(CMember before, CMember after);
    void delete(CMember member);

}
