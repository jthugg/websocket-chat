package neo.chat.api.test.security;

import neo.chat.api.application.auth.service.ChatUserDetailsService;
import neo.chat.api.application.util.MemberContextHolder;
import neo.chat.api.persistence.entity.member.Member;
import neo.chat.api.persistence.repository.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ChatUserDetailsServiceTest {

    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    ChatUserDetailsService chatUserDetailsService;

    @Test
    @Profile("test")
    public void test() {
        // Given
        Member member = new Member("test", "test");
        Optional<Member> wrappedMember = Optional.of(member);

        // When
        Mockito.when(memberRepository.findById(ArgumentMatchers.anyLong())).thenReturn(wrappedMember);

        // Then
        Assertions.assertDoesNotThrow(() -> {
            UserDetails userDetails = chatUserDetailsService.loadUserByUsername(String.valueOf(member.getId()));
            assert MemberContextHolder.getMember() == member;
            assert userDetails.getUsername() == member.getUsername();
        });
    }

}
