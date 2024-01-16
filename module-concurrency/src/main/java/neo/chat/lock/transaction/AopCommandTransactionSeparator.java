package neo.chat.lock.transaction;

import neo.chat.persistence.command.config.CommandDBConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AopCommandTransactionSeparator {

    @Transactional(transactionManager = CommandDBConfig.TRANSACTION_MANAGER, propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

}
