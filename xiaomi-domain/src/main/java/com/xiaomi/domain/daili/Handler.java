package com.xiaomi.domain.daili;
import java.lang.reflect.Method;
/**
 * ClassName: Handler
 * Package: com.xiaomi.domain.daili
 */
public interface Handler extends Cloneable {
    Object handle(Object target, Method method, Object[] args) throws Throwable;
    Handler setNext(Handler next);
    Handler getNext();
    Handler clone();
}

