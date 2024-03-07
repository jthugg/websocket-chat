package neo.chat.library.tsid;

import com.github.f4b6a3.tsid.TsidFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.NumberFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TSIDBenchmarkTest {

    static TsidFactory factory;

    @BeforeAll
    public static void setup1() {
        int tsidNodeId = 0;
        int tsidNodeBits = 0;
        long epochMilli = 1_704_067_200_000L; // 24.JAN.01 00:00:00.000 UTC
        String timezone = "UTC";
        Instant fixedEpoch = Instant.ofEpochMilli(epochMilli);
        factory = TsidFactory.builder()
                .withClock(Clock.fixed(fixedEpoch, ZoneId.of(timezone)))
                .withNode(tsidNodeId)
                .withNodeBits(tsidNodeBits)
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {64, 128, 256, 512, 1024, 2048, 4096, 100_000, 500_000})
    public void creationAgainstUUID(int numbersOfConcurrentRequests) throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();

        List<Callable<Long>> tsidTasks = new ArrayList<>();
        while (tsidTasks.size() < numbersOfConcurrentRequests) {
            tsidTasks.add(() -> factory.create().toLong());
        }

        List<Callable<String>> uuidTasks = new ArrayList<>();
        while (uuidTasks.size() < numbersOfConcurrentRequests) {
            uuidTasks.add(() -> UUID.randomUUID().toString());
        }

        long tsidStart = System.nanoTime();
        service.invokeAll(tsidTasks).forEach(value -> {
            try {
                value.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        long tsidDuration = System.nanoTime() - tsidStart;

        long uuidStart = System.nanoTime();
        service.invokeAll(uuidTasks).forEach(value -> {
            try {
                value.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        long uuidDuration = System.nanoTime() - uuidStart;

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
        System.out.printf("concurrent requests: %s\n", nf.format(numbersOfConcurrentRequests));
        System.out.printf("tsid duration: %s ns\n", nf.format(tsidDuration));
        System.out.printf("uuid duration: %s ns\n", nf.format(uuidDuration));
        System.out.println();
    }

    @ParameterizedTest
    @ValueSource(ints = {64, 128, 256, 512, 1024, 2048, 4096, 100_000, 500_000})
    public void sortAgainstUUID(int numbersOfConcurrentRequests) throws InterruptedException, ExecutionException {
        ExecutorService service = Executors.newCachedThreadPool();

        List<Callable<Long>> tsidTasks = new ArrayList<>();
        while (tsidTasks.size() < numbersOfConcurrentRequests) {
            tsidTasks.add(() -> factory.create().toLong());
        }

        List<Callable<String>> uuidTasks = new ArrayList<>();
        while (uuidTasks.size() < numbersOfConcurrentRequests) {
            uuidTasks.add(() -> UUID.randomUUID().toString());
        }

        List<Future<Long>> tsidFutures = service.invokeAll(tsidTasks);
        List<Future<String>> uuidFutures = service.invokeAll(uuidTasks);

        Set<Long> tsidSet = new TreeSet<>();
        long tsidStart = System.nanoTime();
        for (Future<Long> future : tsidFutures) {
            tsidSet.add(future.get());
        }
        long tsidDuration = System.nanoTime() - tsidStart;

        Set<String> uuidSet = new TreeSet<>();
        long uuidStart = System.nanoTime();
        for (Future<String> future : uuidFutures) {
            uuidSet.add(future.get());
        }
        long uuidDuration = System.nanoTime() - uuidStart;

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
        System.out.printf("concurrent requests: %s\n", nf.format(numbersOfConcurrentRequests));
        System.out.printf("tsid duration: %s ns\n", nf.format(tsidDuration));
        System.out.printf("uuid duration: %s ns\n", nf.format(uuidDuration));
        System.out.println();
    }

}
