package neo.chat.presentation.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import neo.chat.presentation.auth.valid.ValidationMessage;
import neo.chat.presentation.auth.valid.ValidationRegexp;

public record WithdrawRequestDto(
        @NotBlank
        @Pattern(regexp = ValidationRegexp.PASSWORD, message = ValidationMessage.PASSWORD)
        String password
) {}
