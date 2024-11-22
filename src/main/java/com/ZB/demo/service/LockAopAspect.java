package com.ZB.demo.service;

import com.ZB.demo.aop.AccountLockIdInterface;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LockAopAspect {

    private final LockService lockService;

    @Around("@annotation(com.ZB.demo.aop.AccountLock) && args(request)")
    public Object aroundMethod(ProceedingJoinPoint pjp , AccountLockIdInterface request) throws Throwable {
        String accountNumber = request.getAccountNumber();
        lockService.lock(accountNumber);
        try {
            return pjp.proceed();
        }finally {
            lockService.unlock(accountNumber);
        }
    }
}
