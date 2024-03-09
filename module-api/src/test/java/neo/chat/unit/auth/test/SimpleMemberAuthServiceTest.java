package neo.chat.unit.auth.test;

import neo.chat.application.service.auth.service.SimpleMemberAuthService;
import neo.chat.persistence.repository.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SimpleMemberAuthServiceTest {

    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    SimpleMemberAuthService simpleMemberAuthService;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void isUsernameAvailableTest(boolean value) {
        String username = "username";
        Mockito.when(memberRepository.existsByUsername(ArgumentMatchers.any())).thenReturn(value);
        Assertions.assertNotEquals(
                simpleMemberAuthService.isUsernameAvailable(username),
                memberRepository.existsByUsername(username)
        );
    }

}
