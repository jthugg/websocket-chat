package neo.chat.unit.util;

import neo.chat.presentation.auth.valid.ValidationRegexp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class RegexpTest {

    @Test
    @DisplayName("회원 아이디 정규식 테스트: 성공 케이스 - 가능한 모든 문자 조합")
    void usernameRegexpTestCase01() {
        Assertions.assertTrue(Pattern.matches(ValidationRegexp.USERNAME, "t.e-s_t123"));
    }

    @Test
    @DisplayName("회원 아이디 정규식 테스트: 성공 케이스 - 영문 소문자")
    void usernameRegexpTestCase02() {
        Assertions.assertTrue(Pattern.matches(ValidationRegexp.USERNAME, "test"));
    }

    @Test
    @DisplayName("회원 아이디 정규식 테스트: 성공 케이스 - 영문 대문자")
    void usernameRegexpTestCase03() {
        Assertions.assertTrue(Pattern.matches(ValidationRegexp.USERNAME, "TEST"));
    }

    @Test
    @DisplayName("회원 아이디 정규식 테스트: 성공 케이스 - 숫자")
    void usernameRegexpTestCase04() {
        Assertions.assertTrue(Pattern.matches(ValidationRegexp.USERNAME, "1234"));
    }

    @Test
    @DisplayName("회원 아이디 정규식 테스트: 성공 케이스 - 허용된 특수문자")
    void usernameRegexpTestCase05() {
        Assertions.assertTrue(Pattern.matches(ValidationRegexp.USERNAME, ".-_."));
    }

    @Test
    @DisplayName("회원 아이디 정규식 테스트: 실패 케이스 - 너무 짧은 문자열")
    void usernameRegexpTestCase06() {
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.USERNAME, "t1-"));
    }

    @Test
    @DisplayName("회원 아이디 정규식 테스트: 실패 케이스 - 허용되지 않은 문자 포함")
    void usernameRegexpTestCase07() {
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.USERNAME, "t1-가나다"));
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.USERNAME, "t1-!!"));
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.USERNAME, "t1-가나다"));
    }

    @Test
    @DisplayName("회원 아이디 정규식 테스트: 실패 케이스 - 너무 긴 문자열")
    void usernameRegexpTestCase08() {
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.USERNAME, "ttttteeeeessssssttttt"));
    }

    @Test
    @DisplayName("회원 비밀번호 정규식 테스트: 성공 케이스")
    void passwordRegexpTestCase01() {
        Assertions.assertTrue(Pattern.matches(ValidationRegexp.PASSWORD, "TestPassword12!@"));
    }

    @Test
    @DisplayName("회원 비밀번호 정규식 테스트: 실패 케이스 - 특수문자 누락")
    void passwordRegexpTestCase02() {
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.PASSWORD, "TestPassword12"));
    }

    @Test
    @DisplayName("회원 비밀번호 정규식 테스트: 실패 케이스 - 숫자 누락")
    void passwordRegexpTestCase03() {
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.PASSWORD, "TestPassword!@"));
    }

    @Test
    @DisplayName("회원 비밀번호 정규식 테스트: 실패 케이스 - 영문 대문자 누락")
    void passwordRegexpTestCase04() {
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.PASSWORD, "testpassword12!@"));
    }

    @Test
    @DisplayName("회원 비밀번호 정규식 테스트: 실패 케이스 - 영문 소문자 누락")
    void passwordRegexpTestCase05() {
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.PASSWORD, "TESTPASSWORD12!@"));
    }

    @Test
    @DisplayName("회원 비밀번호 정규식 테스트: 실패 케이스 - 허용되지 않는 특수문자 포함")
    void passwordRegexpTestCase06() {
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.PASSWORD, "testpassword12<>"));
    }

    @Test
    @DisplayName("회원 비밀번호 정규식 테스트: 실패 케이스 - 너무 짧은 문자열")
    void passwordRegexpTestCase07() {
        Assertions.assertFalse(Pattern.matches(ValidationRegexp.PASSWORD, "tp12!@"));
    }

    @Test
    @DisplayName("회원 비밀번호 정규식 테스트: 실패 케이스 - 너무 긴 문자열")
    void passwordRegexpTestCase08() {
        Assertions.assertFalse(Pattern.matches(
                ValidationRegexp.PASSWORD,
                "TestPassword12!@TestPassword12!@TestPassword12!@TestPassword12!@"
        ));
    }

}
