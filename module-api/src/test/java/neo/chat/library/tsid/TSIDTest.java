package neo.chat.library.tsid;

import com.github.f4b6a3.tsid.TsidFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

public class TSIDTest {

    @ParameterizedTest
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

}
