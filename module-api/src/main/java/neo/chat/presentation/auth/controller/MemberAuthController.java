package neo.chat.presentation.auth.controller;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import neo.chat.application.service.auth.service.MemberAuthService;
import neo.chat.presentation.auth.valid.ValidationMessage;
import neo.chat.presentation.auth.valid.ValidationRegexp;
import neo.chat.presentation.util.Response;
import neo.chat.settings.route.ApiRoute;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    @GetMapping(ApiRoute.AUTH_CHECK_USERNAME)
    public ResponseEntity<Response<Boolean>> checkUsername(
            @RequestParam
            @Pattern(regexp = ValidationRegexp.USERNAME, message = ValidationMessage.USERNAME)
            String value
    ) {
        return Response.responseEntityOf(HttpStatus.OK, memberAuthService.isUsernameAvailable(value));
    }

}
