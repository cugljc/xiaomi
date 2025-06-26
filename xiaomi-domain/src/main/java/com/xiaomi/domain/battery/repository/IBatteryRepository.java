package com.xiaomi.domain.battery.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiaomi.domain.battery.model.entity.SignalWarnEntity;
import com.xiaomi.domain.battery.model.entity.VehicleInfoEntity;
import com.xiaomi.domain.battery.model.entity.VehicleLatestSignalEntity;
import com.xiaomi.domain.battery.model.entity.WarnRuleEntity;

import java.util.List;
import java.util.Map;

/**
 * ClassName: IBatteryRepository
 * Package: com.xiaomi.domain.battery.repository
 */
public interface IBatteryRepository {
    List<WarnRuleEntity> queryWarnRuleList();

    void storeSearchRateTable(String key, Map<String, String> table);

    void setSearchMax(String key, float maxVal);


    Integer getWarnLevel(String groupKey, String queryKey);

    float getMaxValue(String groupKey);

    void setSearchMaxWarnLevel(String groupKey, int maxLevel);

    Integer getMaxWarnLevel(String key);


    Integer insertVehicleInfo(VehicleInfoEntity vehicleInfoEntity);

    String selectBatteryType(String vid);

    void writeSignal(SignalWarnEntity signalWarnEntity) throws JsonProcessingException;

    List<VehicleLatestSignalEntity> queryBatterySignal(Integer carId);

    List<VehicleLatestSignalEntity> findAllSignals();
}
