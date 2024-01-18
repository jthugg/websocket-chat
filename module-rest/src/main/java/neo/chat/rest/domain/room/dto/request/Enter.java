package neo.chat.rest.domain.room.dto.request;

import jakarta.validation.constraints.Pattern;
import neo.chat.rest.domain.room.dto.InputValidation;

public record Enter(
        @Pattern(regexp = InputValidation.PASSWORD_REGEXP, message = InputValidation.PASSWORD_MESSAGE)
        String password
) {}
