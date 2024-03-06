package neo.chat.api.test.auth;

import neo.chat.api.application.auth.service.SimpleMemberAuthService;
import neo.chat.api.persistence.entity.member.Member;
import neo.chat.api.persistence.repository.member.MemberRepository;
import neo.chat.api.settings.exception.model.authentication.AuthDataMismatchException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class MemberAuthServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    SimpleMemberAuthService simpleMemberAuthService;

    @Test
    @Transactional
    public void registerTest() {
        // Given
        String username = "test";
        String password = "test";
        String encodedPassword = "encodedPassword";
        Member member = new Member(username, encodedPassword);

        // When
        Mockito.when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        Mockito.when(memberRepository.save(ArgumentMatchers.any()))
                .thenReturn(member);

        // Then
        Assertions.assertDoesNotThrow(() -> {
            Member newMember = simpleMemberAuthService.register(username, password);
            Assertions.assertSame(member, newMember);
        });
    }

    @Test
    @Transactional(readOnly = true)
    public void matchUserDataTest() {
        // Given
        String username = "test";
        String password = "test";
        String encodedPassword = "encodedPassword";
        String wrongPassword = "wrongPassword";
        Member member = new Member(username, encodedPassword);

        // When
        Mockito.when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        Mockito.when(passwordEncoder.matches(wrongPassword, encodedPassword)).thenReturn(false);
        Mockito.when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));

        // Then
        Assertions.assertDoesNotThrow(() -> simpleMemberAuthService.matchUserData(username, password));
        Assertions.assertThrows(
                AuthDataMismatchException.class,
                () -> simpleMemberAuthService.matchUserData(username, wrongPassword)
        );
    }

    @Test
    @Transactional(readOnly = true)
    public void getUserDataTest() {
        // Given
        String username = "test";
        String password = "test";
        Member member = new Member(username, password);
        long memberId = member.getId();

        // When
        Mockito.when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // Then
        Assertions.assertDoesNotThrow(() ->
                Assertions.assertSame(member, simpleMemberAuthService.getUserDataById(memberId)));
    }

}
