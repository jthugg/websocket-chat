package neo.chat.unit.auth.test.tx;

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

}
