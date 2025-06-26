package com.xiaomi.infrastructure.persistent.dao;

import com.xiaomi.infrastructure.dbrouter.annotation.DBRouter;
import com.xiaomi.infrastructure.dbrouter.annotation.DBRouterStrategy;
import com.xiaomi.infrastructure.persistent.po.SignalHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: SignalHistoryDao
 * Package: com.xiaomi.infrastructure.persistent.dao
 */
@DBRouterStrategy(splitTable=true)
@Mapper
public interface SignalHistoryDao {
    @DBRouter(key="carId")
    int insert(SignalHistory history);
}
