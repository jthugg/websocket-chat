package neo.chat.rest.domain.member.controller;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import neo.chat.rest.domain.member.dto.InputValidation;
import neo.chat.rest.domain.member.dto.response.UsernameCheckResult;
import neo.chat.rest.domain.member.service.MemberService;
import neo.chat.rest.util.ApiRoute;
import neo.chat.rest.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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

}
