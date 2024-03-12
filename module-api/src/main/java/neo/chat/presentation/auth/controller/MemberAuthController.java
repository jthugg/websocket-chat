package neo.chat.presentation.auth.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import neo.chat.application.service.auth.model.AuthResult;
import neo.chat.application.service.auth.properties.JWTProperties;
import neo.chat.application.service.auth.service.MemberAuthService;
import neo.chat.presentation.auth.dto.request.LoginRequestDto;
import neo.chat.presentation.auth.dto.request.RegisterRequestDto;
import neo.chat.presentation.auth.dto.request.WithdrawRequestDto;
import neo.chat.presentation.auth.dto.response.MemberAuthInfoDto;
import neo.chat.presentation.auth.valid.ValidationMessage;
import neo.chat.presentation.auth.valid.ValidationRegexp;
import neo.chat.presentation.util.Response;
import neo.chat.settings.route.ApiRoute;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

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

    @PostMapping(ApiRoute.AUTH_REGISTER)
    public ResponseEntity<Response<MemberAuthInfoDto>> register(@RequestBody @Valid RegisterRequestDto dto) {
        AuthResult result = memberAuthService.register(dto.username(), dto.password());
        return Response.headedResponseEntityOf(
                HttpStatus.CREATED,
                tokenHeaderConsumer(result),
                new MemberAuthInfoDto(result.member())
        );
    }

    @PostMapping(ApiRoute.AUTH_LOGIN)
    public ResponseEntity<Response<MemberAuthInfoDto>> login(@RequestBody @Valid LoginRequestDto dto) {
        AuthResult result = memberAuthService.login(dto.username(), dto.password());
        return Response.headedResponseEntityOf(
                HttpStatus.OK,
                tokenHeaderConsumer(result),
                new MemberAuthInfoDto(result.member())
        );
    }

    @PostMapping(ApiRoute.AUTH_REISSUE)
    public ResponseEntity<Response<MemberAuthInfoDto>> reissue(
            @CookieValue(JWTProperties.REFRESH_TOKEN) String refreshToken
    ) {
        AuthResult result = memberAuthService.reissue(refreshToken);
        return Response.headedResponseEntityOf(
                HttpStatus.OK,
                tokenHeaderConsumer(result),
                new MemberAuthInfoDto(result.member())
        );
    }

    @PostMapping(ApiRoute.AUTH_LOGOUT)
    public ResponseEntity<Response<Void>> logout() {
        // TODO: Expire Token
        // Expires token via blacklisting on Redis or some other data storage.
        // But do nothing in this version(v1.0.0) but just expire cookie value.
        return Response.voidHeadedResponseEntityOf(HttpStatus.OK, expireRefreshTokenCookie());
    }

    @DeleteMapping(ApiRoute.AUTH_WITHDRAW)
    public ResponseEntity<Response<Void>> withdraw(@RequestBody @Valid WithdrawRequestDto dto) {
        memberAuthService.withdraw(dto.password());
        return Response.voidHeadedResponseEntityOf(HttpStatus.OK, expireRefreshTokenCookie());
    }

    private Consumer<HttpHeaders> tokenHeaderConsumer(AuthResult result) {
        return headers -> {
            headers.setBearerAuth(result.accessToken());
            headers.set(
                    HttpHeaders.SET_COOKIE,
                    ResponseCookie.from(JWTProperties.REFRESH_TOKEN, result.refreshToken())
                            .httpOnly(true)
                            .secure(true)
                            .sameSite(Cookie.SameSite.STRICT.name())
                            .maxAge(result.refreshTokenTTL())
                            .build()
                            .toString()
            );
        };
    }

    private Consumer<HttpHeaders> expireRefreshTokenCookie() {
        return headers -> headers.set(
                HttpHeaders.SET_COOKIE,
                ResponseCookie.from(JWTProperties.REFRESH_TOKEN)
                        .maxAge(0L)
                        .build()
                        .toString()
        );
    }

}
