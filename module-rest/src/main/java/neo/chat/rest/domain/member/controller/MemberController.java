package neo.chat.rest.domain.member.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import neo.chat.jwt.model.TokenSet;
import neo.chat.jwt.service.TokenService;
import neo.chat.jwt.util.InvalidTokenException;
import neo.chat.jwt.util.TokenConstant;
import neo.chat.jwt.util.TokenProperties;
import neo.chat.persistence.command.entity.CMember;
import neo.chat.persistence.query.document.QMember;
import neo.chat.rest.domain.member.dto.InputValidation;
import neo.chat.rest.domain.member.dto.request.Login;
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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @PostMapping(ApiRoute.LOGIN)
    public ResponseEntity<BaseResponse<Member>> login(@RequestBody @Valid Login dto) {
        QMember member = memberService.login(dto.username(), dto.password());
        TokenSet tokenSet = tokenService.generateAccessToken(member.getUsername());
        return BaseResponse.headedResponseEntityOf(
                HttpStatus.OK,
                setTokenHeaders(tokenSet),
                Member.from(member)
        );
    }

    @PostMapping(ApiRoute.REISSUE)
    public ResponseEntity<BaseResponse<Member>> reissue(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String accessToken,
            @CookieValue(TokenConstant.REFRESH_TOKEN) String refreshToken
    ) {
        if (tokenService.isExpired(refreshToken)) {
            throw new InvalidTokenException();
        }
        if (accessToken != null && accessToken.startsWith(TokenConstant.BEARER)) {
            accessToken = accessToken.substring(TokenConstant.BEARER.length());
        }
        tokenService.blacklist(accessToken, refreshToken);
        String username = tokenService.getUsername(refreshToken);
        return BaseResponse.headedResponseEntityOf(
                HttpStatus.OK,
                setTokenHeaders(tokenService.generateAccessToken(username)),
                Member.from(memberService.getMemberByUsername(username))
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
