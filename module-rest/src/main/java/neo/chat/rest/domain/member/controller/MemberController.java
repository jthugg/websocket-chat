package neo.chat.rest.domain.member.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import neo.chat.jwt.model.TokenSet;
import neo.chat.jwt.service.TokenService;
import neo.chat.jwt.util.TokenConstant;
import neo.chat.jwt.util.TokenProperties;
import neo.chat.persistence.command.entity.CMember;
import neo.chat.rest.domain.member.dto.InputValidation;
import neo.chat.rest.domain.member.dto.request.Register;
import neo.chat.rest.domain.member.dto.response.Member;
import neo.chat.rest.domain.member.dto.response.UsernameCheckResult;
import neo.chat.rest.domain.member.service.MemberService;
import neo.chat.rest.util.ApiRoute;
import neo.chat.rest.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

@Validated
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;
    private final TokenProperties tokenProperties;

    @GetMapping(ApiRoute.USERNAME_CHECK)
    public ResponseEntity<BaseResponse<UsernameCheckResult>> checkUsername(
            @RequestParam(name = "username")
            @Pattern(regexp = InputValidation.USERNAME_REGEXP, message = InputValidation.USERNAME_MESSAGE)
            String username
    ) {
        return BaseResponse.responseEntityOf(
                HttpStatus.OK,
                new UsernameCheckResult(memberService.isAvailable(username))
        );
    }

    @PostMapping(ApiRoute.REGISTER)
    public ResponseEntity<BaseResponse<Member>> register(@RequestBody @Valid Register dto) {
        CMember member = memberService.register(dto.username(), dto.password());
        TokenSet tokenSet = tokenService.generateAccessToken(member.getUsername());
        return BaseResponse.headedResponseEntityOf(
                HttpStatus.CREATED,
                setTokenHeaders(tokenSet),
                Member.from(member)
        );
    }

    private Consumer<HttpHeaders> setTokenHeaders(TokenSet tokenSet) {
        return headers -> {
            headers.add(
                    HttpHeaders.AUTHORIZATION,
                    TokenConstant.BEARER.concat(tokenSet.accessToken())
            );
            headers.add(
                    HttpHeaders.SET_COOKIE,
                    ResponseCookie.from(TokenConstant.REFRESH_TOKEN, tokenSet.refreshToken())
                            .httpOnly(true)
                            .secure(true)
                            .sameSite(Cookie.SameSite.STRICT.attributeValue())
                            .maxAge(tokenProperties.getRefreshTokenTTL())
                            .build()
                            .toString()
            );
        };
    }

}
