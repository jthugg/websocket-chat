package neo.chat.unit.room.test.persistence;

import lombok.extern.slf4j.Slf4j;
import neo.chat.persistence.entity.room.Room;
import neo.chat.persistence.repository.room.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Slf4j
@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@Sql(scripts = {"classpath:sql/test/schema.sql", "classpath:sql/test/data.sql"})
public class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;

    @Test
    void testCase01() {
        Room room = roomRepository.findByIdJoinFetchParticipantsWithLock(0L).get();
        log.info("""
                
                id: {}
                title: {}
                password: {}
                capacity: {}
                saturation: {}
                attending: {}
                """,
                room.getId(),
                room.getTitle(),
                room.getPassword(),
                room.getCapacity(),
                room.getSaturation(),
                room.getAttending());
        room.getParticipants().forEach(value -> log.info(value.getNickname()));
    }

    @Test
    void testCase02() {
        roomRepository.findByIdJoinFetchParticipantsWithLock(10L);
    }

}
