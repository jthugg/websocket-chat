package neo.chat.unit.auth.test;

import neo.chat.presentation.auth.valid.ValidationRegexp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.validation.annotation.Validated;

import java.util.regex.Pattern;

@Validated
public class RegexpTest {

    @ParameterizedTest
    @ValueSource(strings = {"t", "test", "test123", "test123!", "test123.test", "testtesttesttesttesttest"})
    void usernameTest(String username) {
        switch (username) {
            case "t", "test123!", "testtesttesttesttesttest" ->
                    Assertions.assertFalse(Pattern.matches(ValidationRegexp.USERNAME, username));
            case "test", "test123", "test123.test" ->
                    Assertions.assertTrue(Pattern.matches(ValidationRegexp.USERNAME, username));
        }
    }

}
