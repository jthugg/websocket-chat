package neo.chat.unit.util;

import neo.chat.presentation.auth.valid.ValidationRegexp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class RegexpTest {

    @Test
    void usernameRegexpTest() {
        Assertions.assertTrue(Pattern.matches(ValidationRegexp.USERNAME, "t.e-s_t123"));
        Assertions.assertTrue(Pattern.matches(ValidationRegexp.USERNAME, "test"));
        Assertions.assertTrue(Pattern.matches(ValidationRegexp.USERNAME, "TEST"));
        Assertions.assertTrue(Pattern.matches(ValidationRegexp.USERNAME, "1234"));
        Assertions.assertTrue(Pattern.matches(ValidationRegexp.USERNAME, ".-_."));
    }

    @Test
    void passwordRegexpTest() {
        Assertions.assertTrue(Pattern.matches(ValidationRegexp.PASSWORD, "TestPassword12!@"));
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.PASSWORD, "TestPassword123"));
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.PASSWORD, "TestPassword!@"));
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.PASSWORD, "TESTPASSWORD12!@"));
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.PASSWORD, "testpassword12!@"));
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.PASSWORD, "TESTPASSWORD12!@"));
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.PASSWORD, "Tp`~!@#$%^&*()_-+="));
    }

}
