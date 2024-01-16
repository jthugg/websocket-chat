package neo.chat.lock.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.chat.lock.annotation.DistributedLock;
import neo.chat.lock.transaction.AopCommandTransactionSeparator;
import neo.chat.lock.transaction.AopQueryTransactionSeparator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

    private final static String LOCK_NAME_FORMAT = "LOCK-%s-%s-%s"; // LOCK-{targetDataSource}-{targetType}-{identifier}

    private final RedissonClient redissonClient;
    private final AopCommandTransactionSeparator aopCommandTransactionSeparator;
    private final AopQueryTransactionSeparator aopQueryTransactionSeparator;

    @Around("@annotation(neo.chat.lock.annotation.DistributedLock) && args(targetId, ..)")
    public Object lock(ProceedingJoinPoint joinPoint, UUID targetId) throws Throwable {
        DistributedLock annotation = ((MethodSignature) joinPoint.getSignature())
                .getMethod()
                .getAnnotation(DistributedLock.class);
        RLock lock = redissonClient.getLock(String.format(
                LOCK_NAME_FORMAT,
                annotation.targetDataSource(),
                annotation.type().name(),
                targetId.toString()
        ));
        try {
            if (lock.tryLock(annotation.timeout(), annotation.holdTime(), annotation.timeUnit())) {
                return switch (annotation.targetDataSource()) {
                    case COMMAND -> aopCommandTransactionSeparator.proceed(joinPoint);
                    case QUERY -> aopQueryTransactionSeparator.proceed(joinPoint);
                };
            }
            return false;
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
