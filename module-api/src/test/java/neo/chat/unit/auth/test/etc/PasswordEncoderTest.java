package neo.chat.unit.auth.test.etc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {

    @Test
    void test() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "test";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        Assertions.assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        String notMatchedRawPassword = "testtest";
        Assertions.assertFalse(passwordEncoder.matches(notMatchedRawPassword, encodedPassword));
    }

}
