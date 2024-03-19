package neo.chat.application.service.room.model;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public record SearchChatRoomRequest(
        boolean isPublicOnly,
        Set<String> keywords,
        int size,
        ChatRoomSortOption sortOption,
        Sort.Direction direction,
        RoomSearchCursor cursor
) {

    private static final int ZERO = 0;

    public Pageable getPageable() {
        if (sortOption == ChatRoomSortOption.DEFAULT) {
            return PageRequest.of(ZERO, size, Sort.by(Sort.Order.desc(ChatRoomSortOption.DEFAULT.getFieldName())));
        }
        Sort.Order order = getOrder(sortOption);
        return PageRequest.of(ZERO, size, Sort.by(
                order,
                Sort.Order.desc(ChatRoomSortOption.DEFAULT.getFieldName())
        ));
    }

    private Sort.Order getOrder(ChatRoomSortOption option) {
        if (direction().isAscending()) {
            return Sort.Order.asc(option.getFieldName());
        }
        return Sort.Order.desc(option.getFieldName());
    }

    public boolean hasCursor() {
        return cursor != null;
    }

}
