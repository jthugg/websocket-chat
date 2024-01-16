package neo.chat.lock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 분산락 애너테이션
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 락 종류
     *
     * @see LockType
     */
    LockType type();

    /**
     * 데이터 소스 타입 command db or query db
     *
     * @see TargetDataSource
     */
    TargetDataSource targetDataSource();

    /**
     * 기본 시간단위 default sec
     *
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 락 최대 대기시간 default 3 sec
     *
     */
    long timeout() default 3L;

    /**
     * 락 최대 점유 시간 default 3 sec
     *
     */
    long holdTime() default 3L;

}
