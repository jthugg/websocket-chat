package neo.chat.unit.util;

import neo.chat.application.util.EntityIdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("엔티티별 식별자 생성기 테스트")
public class EntityIdGeneratorTest {

    @Test
    @DisplayName("서로 다른 인스턴스인지 여부 테스트")
    void isDifferentInstancesTest() {
        EntityIdGenerator[] generators = EntityIdGenerator.values();
        Assertions.assertDoesNotThrow(() -> {
            for (int i = 0; i < generators.length; i++) {
                for (int j = i + 1; j < generators.length; j++) {
                    if (generators[i].getIdGenerator() == generators[j].getIdGenerator()) {
                        throw new RuntimeException();
                    }
                }
            }
        });
    }

}
