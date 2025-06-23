package com.xiaomi.domain.daili;

import java.lang.reflect.Method;

/**
 * ClassName: AbstractHandler
 * Package: com.xiaomi.domain.daili
 */
public abstract class AbstractHandler implements Handler {
    private Handler next;

    @Override
    public Handler setNext(Handler next) {
        this.next = next;
        return next;
    }

    @Override
    public Handler getNext() {
        return next;
    }

    @Override
    public Handler clone() {
        try {
            return (Handler) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }

    /** 子类实现：在调用前后扩展逻辑 */
    protected abstract Object doHandle(Object target, Method method, Object[] args) throws Throwable;

    @Override
    public Object handle(Object target, Method method, Object[] args) throws Throwable {
        Object result;
        try {
            result = doHandle(target, method, args);
        } catch (Throwable t) {
            // 若当前节点未捕获，继续交给下一个节点处理
            if (next != null) return next.handle(target, method, args);
            throw t;
        }
        // 调用下一个节点
        return next != null ? next.handle(target, method, args) : result;
    }
}

