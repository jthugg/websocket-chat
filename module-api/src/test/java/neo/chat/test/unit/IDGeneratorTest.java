package neo.chat.test.unit;

import neo.chat.api.persistence.entity.util.IDGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IDGeneratorTest {

    @Test
    public void duplicationTest() throws InterruptedException {
        int maxThreads = 256;
        ExecutorService service = Executors.newFixedThreadPool(maxThreads);
        List<Callable<Long>> tasks = new ArrayList<>();

        IDGenerator generator = IDGenerator.getInstance();
        while (tasks.size() < maxThreads) {
            tasks.add(() -> generator.generate().toLong());
        }

        Set<Long> results = new HashSet<>();
        service.invokeAll(tasks).parallelStream().forEach(value -> {
            synchronized (results) {
                try {
                    results.add(value.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        assert results.size() == maxThreads;
    }

}
