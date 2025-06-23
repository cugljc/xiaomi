package com.xiaomi.domain.daili;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * ClassName: ProxyUtil
 * Package: com.xiaomi.domain.daili
 */
// ---------- 动态代理工具类 ----------
public class ProxyUtil {
    /**
     * 为指定接口实现创建带责任链扩展的代理
     * @param target    原始实现对象
     * @param handlers  责任链节点列表，执行顺序与列表顺序一致
     * @param <T>       接口类型
     * @return          代理对象
     */

//    给一个“真实对象”+一串“规则节点”（Handler 列表），
//
//    它在每次方法调用前，先克隆出一条新责任链，
//
//    再从链头依次执行各个 Handler（例如：先限流、再捕异常、再上报），
//
//    执行完所有扩展逻辑后，最终反射调用真实对象的方法，并把结果一路返回给调用方。
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, List<Handler> handlers) {
        InvocationHandler invocation = (proxy, method, args) -> {
            // 组装责任链
            Handler head = null, tail = null;
            for (Handler h : handlers) {
                if (head == null) {
                    head = h.clone();
                    tail = head;
                } else {
                    tail.setNext(h.clone());
                    tail = tail.getNext();
                }
            }
            return head == null
                    ? method.invoke(target, args)
                    : head.handle(target, method, args);
        };
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                invocation
        );
    }
}