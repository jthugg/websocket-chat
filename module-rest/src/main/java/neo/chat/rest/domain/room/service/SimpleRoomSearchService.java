package neo.chat.rest.domain.room.service;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.query.RoomQueryRepository;
import neo.chat.persistence.query.config.QueryDBConfig;
import neo.chat.persistence.query.document.QRoom;
import neo.chat.rest.domain.room.dto.request.Search;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SimpleRoomSearchService implements RoomSearchService {

    private final RoomQueryRepository roomQueryRepository;

    @Override
    @Transactional(readOnly = true, transactionManager = QueryDBConfig.TRANSACTION_MANAGER)
    public Slice<QRoom> search(Search dto) {
        return switch (dto.scope()) {
            case TITLE -> roomQueryRepository.searchOnTitle(dto);
            case HOST -> roomQueryRepository.searchOnHost(dto);
        };
    }

}
