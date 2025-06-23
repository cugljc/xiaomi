package com.xiaomi.infrastructure.persistent.dbrouter;

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
