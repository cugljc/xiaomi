package com.xiaomi.infrastructure.dbrouter.dynamic;

import com.xiaomi.infrastructure.dbrouter.DBContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * ClassName: DynamicDataSourceContextHolder
 * Package: com.xiaomi.config
 */

public class DynamicDataSource extends AbstractRoutingDataSource {
    //把配置文件（application.properties、application.yml、
    // 或者环境变量）里 mini-db-router.jdbc.datasource.default 这个 key 对应的值，
    // 注入到 defaultDataSource 这个字段中
    @Value("${mini-db-router.jdbc.datasource.default}")
    private String defaultDataSource;

    @Override
    protected Object determineCurrentLookupKey() {
        if (null == DBContextHolder.getDBKey()) {
            return defaultDataSource;
        } else {
            return "db" + DBContextHolder.getDBKey();
        }
    }

}