package neo.chat.unit.auth.test.persistence;

import lombok.extern.slf4j.Slf4j;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.repository.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@DataJpaTest(showSql = false)
@Rollback(value = false)
@Sql(scripts = {"classpath:sql/test/schema.sql", "classpath:sql/test/data.sql"})
@DisplayName("회원 영속성 레이어 쓰기 테스트")
public class MemberRepositoryWriteTest {

    @Autowired
    MemberRepository memberRepository;

    @Nested
    @DisplayName("회원 등록 테스트")
    class RegisterWithAvailableUsernameTest {
        @Test
        void test() {
            Member member = new Member(100L, "test000", "test000");
            Assertions.assertDoesNotThrow(() -> memberRepository.save(member));
        }
    }

    @Nested
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("회원 아이디 중복 테스트")
    class RegisterWithDuplicatedUsernameTest {
        @Test
        void test() {
            Member member = new Member(100L, "test00", "test00");
            Assertions.assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(member));
        }
    }

}
