//package com.xiaomi.infrastructure.persistent.dynamicdatasource;
//
//import com.xiaomi.types.enums.DatasourceType;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//
///**
// * ClassName: DynamicDataSourceHandlerAspect
// * Package: com.xiaomi.infrastructure.persistent.dynamicdatasource
// */
//@Component
//@Aspect
//public class DynamicDataSourceHandlerAspect {
//    @Pointcut("execution(* com.xiaomi.infrastructure.persistent.dao..*.*(..))")
//    public void pointCut() {
//
//    }
//
//    @Around("pointCut()")
//    public Object around(ProceedingJoinPoint joinPoint) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Class<?> clazz = signature.getMethod().getDeclaringClass();
//        if (clazz.getPackage().getName().endsWith("shard")) {
//            DynamicDataSourceContextHolder.setDataSource(DatasourceType.SHARD);
//        } else {
//            DynamicDataSourceContextHolder.setDataSource(DatasourceType.SHARD2);
//        }
//        try {
//            return joinPoint.proceed();
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        } finally {
//            DynamicDataSourceContextHolder.clearDataSource();
//        }
//    }
//}
