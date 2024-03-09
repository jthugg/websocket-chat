package neo.chat.unit.auth.test;

import lombok.extern.slf4j.Slf4j;
import neo.chat.persistence.repository.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @ParameterizedTest
    @ValueSource(strings = {"nonExistingUsername", "test00", "test01", "test02", "test03", "test04"})
    void test(String username) {
        log.info("current username: {}", username);
        boolean result = memberRepository.existsByUsername(username);
        if (username.equals("nonExistingUsername")) {
            Assertions.assertFalse(result);
            return;
        }
        Assertions.assertTrue(result);
    }

}
