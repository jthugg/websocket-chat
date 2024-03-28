package neo.chat.application.service.room.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import neo.chat.presentation.room.valid.ValidationMessage;
import neo.chat.presentation.room.valid.ValidationRegexp;
import neo.chat.presentation.room.valid.ValidationValue;

public record OpenChatRoomRequest(
        @NotBlank(message = ValidationMessage.CHAT_ROOM_TITLE_NOT_BLANK)
        @Size(max = ValidationValue.CHAT_ROOM_TITLE_LENGTH, message = ValidationMessage.CHAT_ROOM_TITLE_LENGTH)
        String title,
        @Pattern(regexp = ValidationRegexp.CHAT_ROOM_PASSWORD, message = ValidationMessage.CHAT_ROOM_PASSWORD)
        String password,
        @Min(value = ValidationValue.CHAT_ROOM_MIN_CAPACITY, message = ValidationMessage.CHAT_ROOM_CAPACITY)
        @Max(value = ValidationValue.CHAT_ROOM_MAX_CAPACITY, message = ValidationMessage.CHAT_ROOM_CAPACITY)
        int capacity,
        @Size(
                min = ValidationValue.CHAT_ROOM_MIN_NICKNAME_LENGTH,
                max = ValidationValue.CHAT_ROOM_MAX_NICKNAME_LENGTH,
                message = ValidationMessage.CHAT_ROOM_NICKNAME
        )
        String nickname
) {

    public OpenChatRoomRequest(String title, int capacity) {
        this(title, null, capacity, null);
    }

    public OpenChatRoomRequest(String title, String password, int capacity) {
        this(title, password, capacity, null);
    }

    public OpenChatRoomRequest(String title, int capacity, String nickname) {
        this(title, null, capacity, nickname);
    }

    public boolean hasPassword() {
        return password != null;
    }

}
