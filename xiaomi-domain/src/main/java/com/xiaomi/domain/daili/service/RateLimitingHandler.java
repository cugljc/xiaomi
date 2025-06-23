package com.xiaomi.domain.daili.service;

/**
 * ClassName: RateLimitingHandler
 * Package: com.xiaomi.domain.daili.service
 */

import com.xiaomi.domain.daili.AbstractHandler;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/** 简单限流：固定窗口计数 */
public class RateLimitingHandler extends AbstractHandler {
    private final int maxCount;
    private final AtomicInteger counter = new AtomicInteger(0);
    private volatile long windowStart = System.currentTimeMillis();
    private final long windowMillis = 1000L; // 1秒窗口

    public RateLimitingHandler(int maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    protected Object doHandle(Object target, Method method, Object[] args) throws Throwable {
        long now = System.currentTimeMillis();
        synchronized (this) {
            if (now - windowStart >= windowMillis) {
                windowStart = now;
                counter.set(0);
            }
            if (counter.incrementAndGet() > maxCount) {
                throw new RuntimeException("Rate limit exceeded for " + method.getName());
            }
        }
        return method.invoke(target, args);
    }
}

