package aop.logger;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@Aspect
public class CheckTransaction {

    @Before("execution(* dao.impls.*.insert*(..))")
    public void checkTransactionActive() {
        System.out.println(TransactionSynchronizationManager.isActualTransactionActive());
    }

}
