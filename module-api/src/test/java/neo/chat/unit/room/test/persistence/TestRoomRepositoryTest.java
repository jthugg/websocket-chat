package neo.chat.unit.room.test.persistence;

import lombok.extern.slf4j.Slf4j;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.entity.participant.Participant;
import neo.chat.persistence.entity.room.Room;
import neo.chat.util.TestMemberRepository;
import neo.chat.util.TestRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@DisplayName("채팅 방 영속성 레이어 테스트 레포지토리 테스트")
@Sql(scripts = {"classpath:sql/test/schema.sql", "classpath:sql/test/data.sql"})
public class TestRoomRepositoryTest {

    @Autowired
    TestRoomRepository testRoomRepository;
    @Autowired
    TestMemberRepository testMemberRepository;

    @Nested
    @DisplayName("읽기 테스트")
    class ReadTest {

        @Test
        @DisplayName("페치 조인: 호스트 참여자 엔티티까지 조인")
        void readTestCase01() {
            testRoomRepository.findByIsHostAndRemovedAtIsNull().forEach(room -> {
                room.getParticipants().forEach(participant -> {
                    log.info(
                            """
                            
                            title: {}, is active: {}, nickname: {}, is host: {}
                            """,
                            room.getTitle(),
                            !room.isRemoved(),
                            participant.getNickname(),
                            participant.getIsHost()
                    );
                });
            });
        }

        @Test
        @DisplayName("페치 조인: 모든 참여자 엔티티까지 조인")
        void readTestCase02() {
            testRoomRepository.findByRemovedAtIsNull().forEach(room -> {
                room.getParticipants().forEach(participant -> {
                    log.info(
                            """
                            
                            title: {}, is active: {}, nickname: {}, is host: {}
                            """,
                            room.getTitle(),
                            !room.isRemoved(),
                            participant.getNickname(),
                            participant.getIsHost()
                    );
                });
            });
        }

        @Test
        @DisplayName("페치 조인: 모든 참여자 엔티티까지 조인, 사이즈 제한 1")
        void readTestCase03() {
            testRoomRepository.findByRemovedAtIsNullLimit(Pageable.ofSize(1)).forEach(room -> {
                room.getParticipants().forEach(participant -> {
                    log.info(
                            """
                            
                            title: {}, is active: {}, nickname: {}, is host: {}
                            """,
                            room.getTitle(),
                            !room.isRemoved(),
                            participant.getNickname(),
                            participant.getIsHost()
                    );
                });
            });
        }

        @Test
        @DisplayName("페치 조인: 모든 참여자 엔티티까지 조인, 사이즈 제한 2")
        void readTestCase04() {
            testRoomRepository.findByRemovedAtIsNullLimit(Pageable.ofSize(2)).forEach(room -> {
                room.getParticipants().forEach(participant -> {
                    log.info(
                            """
                            
                            title: {}, is active: {}, nickname: {}, is host: {}
                            """,
                            room.getTitle(),
                            !room.isRemoved(),
                            participant.getNickname(),
                            participant.getIsHost()
                    );
                });
            });
        }

        @Test
        @DisplayName("페치 조인: 모든 참여자 엔티티까지 조인, 사이즈 제한 1, distinct 키워드")
        void readTestCase05() {
            testRoomRepository.findDistinctByRemovedAtIsNullLimit(Pageable.ofSize(1))
                    .forEach(room -> room.getParticipants()
                            .forEach(participant -> log.info(
                                    """
                                    
                                    title: {}, is active: {}, nickname: {}, is host: {}
                                    """,
                                    room.getTitle(),
                                    !room.isRemoved(),
                                    participant.getNickname(),
                                    participant.getIsHost()
                            ))
                    );
        }

        @Test
        @DisplayName("페치 조인: 모든 참여자 엔티티까지 조인, 사이즈 제한 2, distinct 키워드")
        void readTestCase06() {
            testRoomRepository.findDistinctByRemovedAtIsNullLimit(Pageable.ofSize(2))
                    .forEach(room -> room.getParticipants()
                            .forEach(participant -> log.info(
                                    """
                                    
                                    title: {}, is active: {}, nickname: {}, is host: {}
                                    """,
                                    room.getTitle(),
                                    !room.isRemoved(),
                                    participant.getNickname(),
                                    participant.getIsHost()
                            ))
                    );
        }

    }

    @Nested
    @Rollback(false)
    @DisplayName("쓰기 테스트")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    class WriteTest {

        Member member;

        @BeforeEach
        void setup() {
            member = testMemberRepository.save(new Member(100L, "test", "test"));
        }

        @Test
        @DisplayName("채팅방 생성: 성공 케이스")
        void createTestCase01() {
            log.info("채팅방 생성 성공 케이스 진입");
            Room room = Room.builder()
                    .id(100L)
                    .title("writeTestRoom")
                    .password(null)
                    .capacity(10)
                    .build();
            room.getParticipants().add(Participant.builder()
                    .id(100L)
                    .nickname("iamTestMember")
                    .isHost(true)
                    .member(member)
                    .room(room)
                    .build());
            testRoomRepository.save(room);
        }

    }

}
