package neo.chat.lock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LockStorageConfig {

    private static final String HOST_FORMAT = "redis://%s:%s";
    private final String host;
    private final int port;

    public LockStorageConfig(
            @Value("${distributedLockStorage.host}") String host,
            @Value("${distributedLockStorage.port}") int port
    ) {
        this.host = host;
        this.port = port;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(String.format(HOST_FORMAT, host, port));
        return Redisson.create(config);
    }

}
