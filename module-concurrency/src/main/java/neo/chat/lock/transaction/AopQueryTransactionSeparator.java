package neo.chat.lock.transaction;

import neo.chat.persistence.query.config.QueryDBConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AopQueryTransactionSeparator {

    @Transactional(transactionManager = QueryDBConfig.TRANSACTION_MANAGER, propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

}
