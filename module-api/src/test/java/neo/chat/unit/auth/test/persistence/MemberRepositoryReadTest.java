package neo.chat.unit.auth.test.persistence;

import lombok.extern.slf4j.Slf4j;
import neo.chat.persistence.repository.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
public class MemberRepositoryReadTest {

    @Autowired
    MemberRepository memberRepository;

    @ParameterizedTest
    @ValueSource(strings = {"nonExistingUsername", "test00", "test01", "test02", "test03", "test04"})
    void existByUsernameTest(String username) {
        log.info("current username: {}", username);
        boolean result = memberRepository.existsByUsername(username);
        if (username.equals("nonExistingUsername")) {
            Assertions.assertFalse(result);
            return;
        }
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"nonExistingUsername", "test00"})
    void findByUsernameTest(String username) {
        log.info("current username: {}", username);
        if (username.equals("nonExistingUsername")) {
            Assertions.assertThrows(
                    RuntimeException.class,
                    () -> memberRepository.findByUsername(username).orElseThrow(RuntimeException::new)
            );
            return;
        }
        Assertions.assertDoesNotThrow(() ->
                memberRepository.findByUsername(username).orElseThrow(RuntimeException::new));
    }

    @Test
    @DisplayName("회원 식별자 조회: 성공 케이스")
    void findByIdAndRemovedAtIsNullTestCase01() {
        long memberId = 0;
        Assertions.assertDoesNotThrow(() -> memberRepository.findByIdAndRemovedAtIsNull(memberId)
                .orElseThrow(RuntimeException::new));
    }

    @Test
    @DisplayName("회원 식별자 조회: 실패 케이스 - 식별자로 찾을 수 없는 경우")
    void findByIdAndRemovedAtIsNullTestCase02() {
        long memberId = 100L;
        Assertions.assertThrows(
                RuntimeException.class,
                () -> memberRepository.findByIdAndRemovedAtIsNull(memberId).orElseThrow(RuntimeException::new)
        );
    }

    @Test
    @DisplayName("회원 식별자 조회: 실패 케이스 - 탈퇴한 회원")
    void findByIdAndRemovedAtIsNullTestCase03() {
        long memberId = 5L;
        Assertions.assertThrows(
                RuntimeException.class,
                () -> memberRepository.findByIdAndRemovedAtIsNull(memberId).orElseThrow(RuntimeException::new)
        );
    }

}
