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

}
