package neo.chat.unit.room.test.service;

import lombok.extern.slf4j.Slf4j;
import neo.chat.application.service.room.exception.ChatRoomHasNoVacancyException;
import neo.chat.application.service.room.model.EnterChatRoomRequest;
import neo.chat.application.service.room.service.ChatRoomService;
import neo.chat.persistence.entity.room.Room;
import neo.chat.persistence.repository.member.MemberRepository;
import neo.chat.persistence.repository.room.RoomRepository;
import neo.chat.settings.context.AuthMemberContextHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@DisplayName("채팅 방 서비스 레이어 동시성 테스트")
@Sql(scripts = {"classpath:sql/test/schema.sql", "classpath:sql/test/data.sql"})
public class ChatRoomServiceConcurrencyTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ChatRoomService chatRoomService;

    @Test
    void enterRoomTest() throws InterruptedException {
        int numberOfThreads = 50;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);

        List<Callable<Room>> callables = new ArrayList<>();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (long i = 101; i < 121; i++) {
            long id = i;
            callables.add(() -> {
                try {
                    AuthMemberContextHolder.set(memberRepository.findById(id).get());
                    Room room = chatRoomService.enterRoom(new EnterChatRoomRequest(
                            10L,
                            "iam" + id,
                            "test"));
                    successCount.getAndIncrement();
                    return room;
                } catch (ChatRoomHasNoVacancyException e) {
                    log.info("방이 꽉 찼습니다.");
                    failCount.getAndIncrement();
                    return null;
                }
            });
        }

        service.invokeAll(callables);

        Thread.sleep(1_000);
        log.info("final attending: {}", roomRepository.findById(10L).get().getAttending());
        log.info("성공: {}\n실패: {}", successCount, failCount);
    }

    @Test
    void leaveRoomTest() throws InterruptedException {
        long[] memberIds = LongStream.rangeClosed(11, 60).toArray();

        int numbersOfThreads = 50;
        ExecutorService service = Executors.newFixedThreadPool(numbersOfThreads);
        List<Callable<Void>> callables = new ArrayList<>();

        while (callables.size() < numbersOfThreads) {
            int id = callables.size();
            callables.add(() -> {
                AuthMemberContextHolder.set(memberRepository.findById(memberIds[id]).get());
                chatRoomService.leaveRoom(9L);
                return null;
            });
        }

        service.invokeAll(callables);

        Thread.sleep(1_000);
        log.info("final attending: {}", roomRepository.findById(9L).get().getAttending());
    }

}
