package neo.chat.unit.auth.test.tx;

import neo.chat.application.service.auth.exception.MemberNotFoundException;
import neo.chat.application.service.auth.tx.MemberAuthTransactionScript;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.repository.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MemberAuthTransactionScriptTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    MemberAuthTransactionScript memberAuthTransactionScript;

    @Test
    @DisplayName("회원 레코드 생성: 성공 케이스")
    void createMemberTestCase01() {
        String username = "test";
        String password = "test";
        String encodedPassword = "encodedPassword";
        Member member = new Member(100L, username, password);

        Mockito.when(memberRepository.save(ArgumentMatchers.any())).thenReturn(member);
        Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn(encodedPassword);

        Assertions.assertSame(memberAuthTransactionScript.createMember(username, password), member);
        Assertions.assertDoesNotThrow(() -> memberAuthTransactionScript.createMember(username, password));
    }

    @Test
    @DisplayName("회원 레코드 생성: 실패 케이스 - 테이블 제약조건 위반")
    void createMEmberTestCase02() {
        String username = "test";
        String password = "test";

        Mockito.when(memberRepository.save(ArgumentMatchers.any())).thenThrow(DataIntegrityViolationException.class);

        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> memberAuthTransactionScript.createMember(username, password)
        );
    }

    @Test
    @DisplayName("회원 식별자 조회: 성공 케이스")
    void readMemberByIdTestCase01() {
        long memberId = 100L;
        Member member = new Member(memberId, "test", "test");

        Mockito.when(memberRepository.findByIdAndRemovedAtIsNull(memberId)).thenReturn(Optional.of(member));

        Assertions.assertDoesNotThrow(() -> memberAuthTransactionScript.readMemberById(memberId));
    }

    @Test
    @DisplayName("회원 식별자 조회: 실패 케이스 - 주어진 식별자로 회원을 조회할 수 없는 경우")
    void readMemberByIdTestCase02() {
        long memberId = 100L;

        Mockito.when(memberRepository.findByIdAndRemovedAtIsNull(memberId)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                MemberNotFoundException.class,
                () -> memberAuthTransactionScript.readMemberById(memberId)
        );
    }

}
