//package com.xiaomi.infrastructure.persistent.dynamicdatasource;
//
//import com.xiaomi.types.enums.DatasourceType;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//
///**
// * ClassName: DynamicDataSourceContextHolder
// * Package: com.xiaomi.config
// */
//public class DynamicDataSourceContextHolder extends AbstractRoutingDataSource {
//    private static final ThreadLocal<DatasourceType> contextHolder = new ThreadLocal<>();
//
//    public static void clearDataSource() {
//        contextHolder.remove();
//    }
//
//    public static void setDataSource(DatasourceType type) {
//        contextHolder.set(type);
//    }
//
//    @Override
//    protected Object determineCurrentLookupKey() {
//        return contextHolder.get();
//    }
//}