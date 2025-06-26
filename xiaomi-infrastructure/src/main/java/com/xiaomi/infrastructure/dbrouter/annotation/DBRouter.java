package com.xiaomi.infrastructure.dbrouter.annotation;

import java.lang.annotation.*;

/**
 * ClassName: DBRouter
 * Package: com.xiaomi.config
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DBRouter {

    String key() default "";

}
