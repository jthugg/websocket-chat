package neo.chat.rest.domain.member.dto.request;

import jakarta.validation.constraints.Pattern;
import neo.chat.rest.domain.member.dto.InputValidation;

public record Login(
        @Pattern(regexp = InputValidation.USERNAME_REGEXP, message = InputValidation.USERNAME_MESSAGE)
        String username,
        @Pattern(regexp = InputValidation.PASSWORD_REGEXP, message = InputValidation.PASSWORD_MESSAGE)
        String password
) {}
