package neo.chat.unit.room.test.persistence;

import lombok.extern.slf4j.Slf4j;
import neo.chat.application.service.room.model.ChatRoomSortOption;
import neo.chat.persistence.entity.member.Member;
import neo.chat.persistence.repository.member.MemberRepository;
import neo.chat.persistence.repository.participant.ParticipantRepository;
import neo.chat.persistence.repository.room.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Slf4j
@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@Sql(scripts = {"classpath:sql/test/schema.sql", "classpath:sql/test/data.sql"})
public class ParticipantRepositoryTest {

    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoomRepository roomRepository;

    @Test
    void testCase00() {
        Member member = memberRepository.findById(0L).get();
        participantRepository.findByMemberAndRemovedAtIsNull(
                member,
                PageRequest.of(
                        0,
                        3,
                        Sort.Direction.DESC,
                        ChatRoomSortOption.DEFAULT.getFieldName()
                )
        ).forEach(value -> log.info("""
                
                participant id: {}
                room id: {}
                """, value.getId(), value.getRoom().getId()));
    }

    @Test
    void testCase01() {
        Member member = memberRepository.findById(0L).get();
        participantRepository.findByMemberAndRemovedAtIsNull(
                member,
                PageRequest.of(
                        0,
                        6,
                        Sort.Direction.DESC,
                        ChatRoomSortOption.DEFAULT.getFieldName()
                )
        ).forEach(value -> log.info("""
                
                participant id: {}
                room id: {}
                """, value.getId(), value.getRoom().getId()));
    }

    @Test
    void testCase02() {
        Member member = memberRepository.findById(0L).get();
        participantRepository.findByMemberAndIdLessThanAndRemovedAtIsNull(
                member,
                28L,
                PageRequest.of(
                        0,
                        3,
                        Sort.Direction.DESC,
                        ChatRoomSortOption.DEFAULT.getFieldName()
                )
        ).forEach(value -> log.info("""
                
                participant id: {}
                room id: {}
                """, value.getId(), value.getRoom().getId()));
    }

    @Test
    void testCase03() {
        Member member = memberRepository.findById(0L).get();
        log.info("is exists: {}", participantRepository.existsByMemberAndRoomIdAndRemovedAtIsNull(member, 0L));
    }

}
