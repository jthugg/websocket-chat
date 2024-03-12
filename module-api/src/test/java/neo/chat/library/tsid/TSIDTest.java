package neo.chat.library.tsid;

import com.github.f4b6a3.tsid.TsidFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.NumberFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@DisplayName("TSID 라이브러리 테스트")
public class TSIDTest {

    @ParameterizedTest
    @DisplayName("노드 식별자 유지 여부 테스트")
    @ValueSource(ints = {256, 512, 1024, 100_000, 500_000})
    void fixedNodeIdTest(int numberOfConcurrentRequests) throws InterruptedException {
        long epochMilli = 1_704_067_200_000L; // 24.JAN.01 00:00:00.000 UTC
        String timezone = "UTC";
        Instant fixed = Instant.ofEpochMilli(epochMilli);

        int nodeId = 0;
        int nodeBits = 12;
        TsidFactory factory0 = TsidFactory.builder()
                .withClock(Clock.fixed(fixed, ZoneId.of(timezone)))
                .withNode(nodeId)
                .withNodeBits(nodeBits)
                .build();

        ExecutorService service = Executors.newCachedThreadPool();
        List<Callable<Long>> callable = new ArrayList<>();
        while (callable.size() < numberOfConcurrentRequests) {
            callable.add(() -> factory0.create().toLong());
        }

        service.invokeAll(callable).forEach(value -> {
            try {
                long id = value.get();
                String bits = Long.toBinaryString(id);
                bits = bits.substring(bits.length() - 22);
                assert Long.parseLong(bits.substring(0, nodeBits)) == nodeId: "노드 식별자가 일치하지 않습니다.";
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("중복 발생 테스트")
    @ValueSource(ints = {256, 512, 1024, 100_000, 500_000})
    void duplicationTest(int numberOfConcurrentRequests) throws InterruptedException {
        long epochMilli = 1_704_067_200_000L; // 24.JAN.01 00:00:00.000 UTC
        String timezone = "UTC";
        Instant fixed = Instant.ofEpochMilli(epochMilli);

        int nodeId = 0;
        int nodeBits = 12;
        TsidFactory factory0 = TsidFactory.builder()
                .withClock(Clock.fixed(fixed, ZoneId.of(timezone)))
                .withNode(nodeId)
                .withNodeBits(nodeBits)
                .build();

        ExecutorService service = Executors.newCachedThreadPool();
        List<Callable<Long>> callable = new ArrayList<>();
        while (callable.size() < numberOfConcurrentRequests) {
            callable.add(() -> factory0.create().toLong());
        }

        Set<Long> ids = new HashSet<>();
        service.invokeAll(callable).parallelStream().forEach(value -> {
            try {
                long id = value.get();
                synchronized (ids) {
                    ids.add(id);
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        assert ids.size() == numberOfConcurrentRequests: "식별자 값이 중복됐습니다.";
    }

    @ParameterizedTest
    @DisplayName("싱글톤, 다중, 새 객체 할당 방식 속도 비교 테스트")
    @ValueSource(ints = {128, 256, 512, 1024})
    public void singleInstanceVsMultiInstance(int numberOfConcurrentRequests) throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();

        int maxInstance = 10;
        List<TsidFactory> factories = new ArrayList<>();
        while (factories.size() < maxInstance) {
            factories.add(TsidFactory.builder().withNode(0).withNodeBits(16).build());
        }

        List<Callable<Long>> singleTasks = new ArrayList<>();
        TsidFactory singleFactory = factories.get(0);
        while (singleTasks.size() < numberOfConcurrentRequests) {
            singleTasks.add(() -> singleFactory.create().toLong());
        }

        List<Callable<Long>> multiTasks = new ArrayList<>();
        while (multiTasks.size() < numberOfConcurrentRequests) {
            for (TsidFactory factory : factories) {
                multiTasks.add(() -> factory.create().toLong());
            }
        }

        List<Callable<Long>> newTasks = new ArrayList<>();
        while (newTasks.size() < numberOfConcurrentRequests) {
            newTasks.add(() -> TsidFactory.builder().withNode(0).withNodeBits(16).build().create().toLong());
        }

        long singleStart = System.nanoTime();
        service.invokeAll(singleTasks);
        long singleDuration = System.nanoTime() - singleStart;
        long multiStart = System.nanoTime();
        service.invokeAll(multiTasks);
        long multiDuration = System.nanoTime() - multiStart;
        long newStart = System.nanoTime();
        service.invokeAll(newTasks);
        long newDuration = System.nanoTime() - newStart;

        NumberFormat nf = NumberFormat.getInstance();
        log.info("single dutation: {} ns", nf.format(singleDuration));
        log.info("multi dutation: {} ns", nf.format(multiDuration));
        log.info("new dutation: {} ns", nf.format(newDuration));
    }

}
