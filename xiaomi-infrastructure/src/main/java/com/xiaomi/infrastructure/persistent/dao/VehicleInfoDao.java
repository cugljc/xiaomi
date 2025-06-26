package com.xiaomi.infrastructure.persistent.dao;

import com.xiaomi.infrastructure.dbrouter.annotation.DBRouter;
import com.xiaomi.infrastructure.dbrouter.annotation.DBRouterStrategy;
import com.xiaomi.infrastructure.persistent.po.VehicleInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: VehicleInfoDao
 * Package: com.xiaomi.infrastructure.persistent.dao
 */
@Mapper
public interface VehicleInfoDao {
    @DBRouter(key="carId")
    String selectByCarId(String carId);
    @DBRouter(key="carId")
    int insert(VehicleInfo vehicleInfo);

}
