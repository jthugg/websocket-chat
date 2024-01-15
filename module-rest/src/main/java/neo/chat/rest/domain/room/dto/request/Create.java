package neo.chat.rest.domain.room.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import neo.chat.rest.domain.room.dto.InputValidation;

public record Create(
        @NotBlank
        @Pattern(regexp = InputValidation.TITLE_REGEXP, message = InputValidation.TITLE_MESSAGE)
        String title,
        @Min(value = InputValidation.MINIMUM_CAPACITY, message = InputValidation.CAPACITY_MESSAGE)
        @Max(value = InputValidation.MAXIMUM_CAPACITY, message = InputValidation.CAPACITY_MESSAGE)
        int capacity,
        @Pattern(regexp = InputValidation.PASSWORD_REGEXP, message = InputValidation.PASSWORD_MESSAGE)
        String password
) {}
