package neo.chat.sync.domain.member.service;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.command.entity.CMember;
import neo.chat.persistence.query.MemberQueryRepository;
import neo.chat.persistence.query.config.QueryDBConfig;
import neo.chat.persistence.query.document.QMember;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SimpleMemberSyncService implements MemberSyncService {

    private final MemberQueryRepository memberQueryRepository;

    @Override
    @Transactional(transactionManager = QueryDBConfig.TRANSACTION_MANAGER)
    public void create(CMember member) {
        memberQueryRepository.save(new QMember(
                member.getId(),
                member.getCreatedAt(),
                member.getUsername(),
                member.getPassword(),
                member.getRole().name()
        ));
    }

    @Override
    @Transactional(transactionManager = QueryDBConfig.TRANSACTION_MANAGER)
    public void update(CMember before, CMember after) {
        assert before.getId() != null;
        memberQueryRepository.findById(before.getId()).ifPresent(member -> {
            member.setPassword(after.getPassword());
            memberQueryRepository.save(member);
        });
    }

    @Override
    @Transactional(transactionManager = QueryDBConfig.TRANSACTION_MANAGER)
    public void delete(CMember member) {
        assert member.getId() != null;
        memberQueryRepository.deleteById(member.getId());
    }

}
