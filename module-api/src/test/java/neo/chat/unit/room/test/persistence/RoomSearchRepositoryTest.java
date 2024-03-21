package neo.chat.unit.room.test.persistence;

import lombok.extern.slf4j.Slf4j;
import neo.chat.application.service.room.model.ChatRoomSortOption;
import neo.chat.application.service.room.model.RoomSearchCursor;
import neo.chat.application.service.room.model.SearchChatRoomRequest;
import neo.chat.persistence.entity.room.Room;
import neo.chat.persistence.repository.room.RoomSearchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;

@Slf4j
@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@DisplayName("채팅 방 검색 레포지토리 테스트")
@Sql(scripts = {"classpath:sql/test/schema.sql", "classpath:sql/test/data.sql"})
public class RoomSearchRepositoryTest {

    @Autowired
    RoomSearchRepository roomSearchRepository;

    @Test
    @DisplayName("커서 페이징 테스트: 성공 케이스 - 기본 정렬")
    void testCase00() {
        Room room = roomSearchRepository.findById(13L).get();
        SearchChatRoomRequest request = new SearchChatRoomRequest(
                false,
                Set.of("test", "title"),
                5,
                ChatRoomSortOption.DEFAULT,
                Sort.Direction.DESC,
                new RoomSearchCursor(room.getId().toString(), room.getId().toString())
        );

        roomSearchRepository.searchRoom(request).forEach(value -> log.info(
                String.format("%s\n%s\n%s", value.getId(), value.getTitle(), value.getSaturation())
        ));
    }

    @Test
    @DisplayName("커서 페이징 테스트: 성공 케이스 - 생성일 정렬")
    void testCase01() {
        Room room = roomSearchRepository.findById(13L).get();
        SearchChatRoomRequest request = new SearchChatRoomRequest(
                false,
                Set.of("test", "title"),
                5,
                ChatRoomSortOption.DATE,
                Sort.Direction.DESC,
                new RoomSearchCursor(room.getId().toString(), room.getCreatedAt().toString())
        );

        roomSearchRepository.searchRoom(request).forEach(value -> log.info(
                String.format("%s\n%s\n%s", value.getId(), value.getTitle(), value.getSaturation())
        ));
    }

    @Test
    @DisplayName("커서 페이징 테스트: 성공 케이스 - 제목순 정렬")
    void testCase02() {
        Room room = roomSearchRepository.findById(13L).get();
        SearchChatRoomRequest request = new SearchChatRoomRequest(
                false,
                Set.of("test", "title"),
                5,
                ChatRoomSortOption.TITLE,
                Sort.Direction.DESC,
                new RoomSearchCursor(room.getId().toString(), room.getTitle())
        );

        roomSearchRepository.searchRoom(request).forEach(value -> log.info(
                String.format("%s\n%s\n%s", value.getId(), value.getTitle(), value.getSaturation())
        ));
    }

    @Test
    @DisplayName("커서 페이징 테스트: 성공 케이스 - 최대인원순 정렬")
    void testCase03() {
        Room room = roomSearchRepository.findById(13L).get();
        SearchChatRoomRequest request = new SearchChatRoomRequest(
                false,
                Set.of("test", "title"),
                5,
                ChatRoomSortOption.CAPACITY,
                Sort.Direction.DESC,
                new RoomSearchCursor(room.getId().toString(), room.getCapacity().toString())
        );

        roomSearchRepository.searchRoom(request).forEach(value -> log.info(
                String.format("%s\n%s\n%s", value.getId(), value.getTitle(), value.getSaturation())
        ));
    }

    @Test
    @DisplayName("커서 페이징 테스트: 성공 케이스 - 포화도순 정렬")
    void testCase04() {
        Room room = roomSearchRepository.findById(13L).get();

        SearchChatRoomRequest request = new SearchChatRoomRequest(
                false,
                Set.of(),
                10,
                ChatRoomSortOption.SATURATION,
                Sort.Direction.DESC,
                new RoomSearchCursor(room.getId().toString(), room.getSaturation().toString())
                //null
        );

        roomSearchRepository.searchRoom(request).forEach(value -> log.info(
                String.format("%s\n%s\n%s", value.getId(), value.getTitle(), value.getSaturation())
        ));
    }

    @Test
    @DisplayName("커서 페이징 테스트: 성공 케이스 - 참여인원순 정렬")
    void testCase05() {
        Room room = roomSearchRepository.findById(13L).get();
        SearchChatRoomRequest request = new SearchChatRoomRequest(
                false,
                Set.of("test", "title"),
                5,
                ChatRoomSortOption.ATTENDING,
                Sort.Direction.DESC,
                new RoomSearchCursor(room.getId().toString(), room.getAttending().toString())
        );

        roomSearchRepository.searchRoom(request).forEach(value -> log.info(
                String.format("%s\n%s\n%s", value.getId(), value.getTitle(), value.getSaturation())
        ));
    }

}
