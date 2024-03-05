package neo.chat.test.unit;

import neo.chat.api.persistence.entity.util.EntityIDGenerator;
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

public class EntityIDGeneratorTest {

    @Test
    public void isSingletonTest() throws InterruptedException {
        int maxThreads = 256;
        ExecutorService service = Executors.newFixedThreadPool(maxThreads);
        List<Callable<IDGenerator>> tasks = new ArrayList<>();
        while (tasks.size() < maxThreads) {
            tasks.add(EntityIDGenerator.MEMBER::getGenerator);
        }

        Set<IDGenerator> results = new HashSet<>();
        service.invokeAll(tasks).parallelStream().forEach(generator -> {
            synchronized (results) {
                try {
                    results.add(generator.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        assert results.size() == 1;
    }

}
