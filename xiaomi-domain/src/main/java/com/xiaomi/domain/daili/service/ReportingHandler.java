package com.xiaomi.domain.daili.service;

/**
 * ClassName: ExceptionLoggingHandler
 * Package: com.xiaomi.domain.daili.service
 */

import com.xiaomi.domain.daili.AbstractHandler;

import java.lang.reflect.Method;

/** 接口调用上报 */
public class ReportingHandler extends AbstractHandler {
    @Override
    protected Object doHandle(Object target, Method method, Object[] args) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = method.invoke(target, args);
        long cost = System.currentTimeMillis() - start;
        System.out.println("[Report] " + method.getName() + " cost=" + cost + "ms");
        return result;
    }
}
