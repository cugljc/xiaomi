package com.xiaomi.infrastructure.persistent.dbrouter;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * ClassName: DynamicDataSourceContextHolder
 * Package: com.xiaomi.config
 */

public class DynamicDataSource extends AbstractRoutingDataSource {
    //无论库什么名字均可以，在xml文件声明db01 db02即可
    @Override
    protected Object determineCurrentLookupKey() {
        return "db" + DBContextHolder.getDBKey();
    }

}