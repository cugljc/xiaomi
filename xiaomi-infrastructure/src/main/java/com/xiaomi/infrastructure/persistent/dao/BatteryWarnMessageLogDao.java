package com.xiaomi.infrastructure.persistent.dao;

import com.xiaomi.domain.task.model.BatteryWarnMessageLogEntity;
import com.xiaomi.infrastructure.persistent.po.BatteryWarnMessageLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: BatteryWarnMessageLogDao
 * Package: com.xiaomi.infrastructure.persistent.dao
 */
@Mapper
public interface BatteryWarnMessageLogDao {

    int insert(BatteryWarnMessageLog log);

    int markSuccessByCarId(String carId);

    List<BatteryWarnMessageLog> findPendingLogs(int limit);

    int increaseRetryCount(Long id);
}

