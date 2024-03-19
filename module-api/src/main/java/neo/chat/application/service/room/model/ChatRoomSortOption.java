package neo.chat.application.service.room.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatRoomSortOption {

    DEFAULT("id"),
    DATE("createdAt"),
    TITLE("title"),
    CAPACITY("capacity"),
    SATURATION("saturation"),
    ATTENDING("attending"),
    ;

    private final String fieldName;

}
