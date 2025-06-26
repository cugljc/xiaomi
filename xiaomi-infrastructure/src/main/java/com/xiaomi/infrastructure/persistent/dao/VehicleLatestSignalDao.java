package com.xiaomi.infrastructure.persistent.dao;

import com.xiaomi.infrastructure.dbrouter.annotation.DBRouter;
import com.xiaomi.infrastructure.persistent.po.VehicleLatestSignal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: VehicleLatestSignalDao
 * Package: com.xiaomi.infrastructure.persistent.dao
 */
@Mapper
public interface VehicleLatestSignalDao {
    @DBRouter(key="carId")
    int insert(VehicleLatestSignal vehicleLatestSignal);
    @DBRouter(key="carId")
    int update(VehicleLatestSignal vehicleLatestSignal);
    @DBRouter(key="carId")
    List<VehicleLatestSignal> selectByCarId(String carId);

    List<VehicleLatestSignal> findAll();
}