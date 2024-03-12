package neo.chat.unit.auth.test.etc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@DisplayName("비밀번호 인코딩 테스트")
public class PasswordEncoderTest {

    @Test
    @DisplayName("인코딩, RAW 문자열 매치 테스트")
    void test() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "test";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        Assertions.assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        String notMatchedRawPassword = "testtest";
        Assertions.assertFalse(passwordEncoder.matches(notMatchedRawPassword, encodedPassword));
        log.info("\n\n\nraw: {}\nencoded: {}\n\n", rawPassword, encodedPassword);
    }

}
