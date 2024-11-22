package com.ZB.demo.service;

import com.ZB.demo.exception.AccountLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {
    private final RedissonClient redissonClient;

    public void lock(String accountNumber) {
        RLock lock = redissonClient.getLock(getLockKey(accountNumber));

        try {
            boolean isLock = lock.tryLock(1, 5, TimeUnit.SECONDS);
            if(!isLock) {
                log.error("======Lock acquisition failed=====");
                throw new AccountLockException("해당 계좌는 사용중입니다.");
            }
        } catch (Exception e) {
            log.error("Redis lock failed");
        }
    }

    public void unlock(String accountNumber) {
        redissonClient.getLock(getLockKey(accountNumber)).unlock();
    }

    private String getLockKey(String accountNumber) {
        return "ACLK: "+accountNumber;
    }
}
