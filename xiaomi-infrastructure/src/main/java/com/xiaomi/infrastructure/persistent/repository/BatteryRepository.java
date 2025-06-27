package com.xiaomi.infrastructure.persistent.repository;

import com.xiaomi.domain.battery.model.entity.VehicleLatestSignalEntity;
import com.xiaomi.infrastructure.dbrouter.DBContextHolder;
import com.xiaomi.infrastructure.dbrouter.DBRouterConfig;
import com.xiaomi.infrastructure.persistent.dao.SignalHistoryDao;
import com.xiaomi.infrastructure.persistent.dao.VehicleLatestSignalDao;
import com.xiaomi.types.enums.ResponseCode;
import com.xiaomi.types.common.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiaomi.domain.battery.model.entity.SignalWarnEntity;
import com.xiaomi.domain.battery.model.entity.VehicleInfoEntity;
import com.xiaomi.domain.battery.model.entity.WarnRuleEntity;
import com.xiaomi.domain.battery.repository.IBatteryRepository;
import com.xiaomi.infrastructure.dbrouter.strategy.IDBRouterStrategy;
import com.xiaomi.infrastructure.persistent.dao.VehicleInfoDao;
import com.xiaomi.infrastructure.persistent.dao.WarningRulesDao;
import com.xiaomi.infrastructure.persistent.po.SignalHistory;
import com.xiaomi.infrastructure.persistent.po.VehicleInfo;
import com.xiaomi.infrastructure.persistent.po.VehicleLatestSignal;
import com.xiaomi.infrastructure.persistent.po.WarningRule;
import com.xiaomi.infrastructure.persistent.redis.IRedisService;
import com.xiaomi.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: BatteryRepository
 * Package: com.xiaomi.infrastructure.persistent.repository
 */
@Slf4j
@Repository
public class BatteryRepository implements IBatteryRepository {
    @Resource
    WarningRulesDao warningRulesDao;
    @Resource
    IRedisService redisService;
    @Resource
    VehicleInfoDao vehicleInfoDao;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private VehicleLatestSignalDao vehicleLatestSignalDao;
    @Resource
    private SignalHistoryDao signalHistoryDao;
    @Resource
    private ApplicationEventPublisher eventPublisher;
    @Resource
    private DBRouterConfig dbRouterConfig;
    @Override
    public List<WarnRuleEntity> queryWarnRuleList(){
        List<WarningRule> warningRules=warningRulesDao.selectrules();
        List<WarnRuleEntity> warningRuleEntities = new ArrayList<>(warningRules.size());
        for (WarningRule warningRule : warningRules) {
            WarnRuleEntity warnRuleEntity = WarnRuleEntity.builder()
                        .id(warningRule.getId())
                        .warnId(warningRule.getWarnId())
                        .batteryType(warningRule.getBatteryType())
                        .warnLevel(warningRule.getWarnLevel())
                        .minVal(warningRule.getMinVal())
                        .maxVal(warningRule.getMaxVal())
                        .warnName(warningRule.getWarnName())
                        .build();
            warningRuleEntities.add(warnRuleEntity);
        }
        return warningRuleEntities;
    }
    @Override
    public void storeSearchRateTable(String key, Map<String, String> table){
        String hashKey = "rules:" + key;
        RMap<String, String> redisMap = redisService.getMap(hashKey);
        redisMap.clear();
        redisMap.putAll(table);
    }
    @Override
    public void setSearchMax(String key, float maxVal){
        // 存储极值
        redisService.setValue("rules:" + key + ":max", String.valueOf(maxVal));
    }
    /**
     * 获取值对应的告警级别
     */
    @Override
    public Integer getWarnLevel(String key, String query) {
        String hashKey = "rules:" + key;
        // 从Redis获取
        RMap<String, String> redisMap = redisService.getMap(hashKey);
        String level = redisMap.get(query);
        return level != null ? Integer.parseInt(level) : null;
    }
    /**
     * 获取极值
     */
    @Override
    public float getMaxValue(String key) {
        String value =redisService.getValue("rules:" + key + ":max");
        return Float.parseFloat(value);
    }
    @Override
    public void setSearchMaxWarnLevel(String key, int maxLevel) {
        redisService.setValue("rules:" + key + ":maxwarnlevel", maxLevel);
    }
    @Override
    public Integer getMaxWarnLevel(String key) {
        return redisService.getValue("rules:" + key + ":maxwarnlevel");
    }


    @Override
    public Integer insertVehicleInfo(VehicleInfoEntity vehicleInfoEntity){
        VehicleInfo vehicleInfo = VehicleInfo.builder()
                .id(vehicleInfoEntity.getId())
                .vid(vehicleInfoEntity.getVid())
                .carId(vehicleInfoEntity.getCarId())
                .batteryType(vehicleInfoEntity.getBatteryType())
                .totalMileageKm(vehicleInfoEntity.getTotalMileageKm())
                .healthPct(vehicleInfoEntity.getHealthPct())
                .build();
        return vehicleInfoDao.insert(vehicleInfo);
    }

    @Override
    public String selectBatteryType(String carId){
        return vehicleInfoDao.selectByCarId(carId);
    }

    @Override
    public void writeSignal(SignalWarnEntity signalWarnEntity) throws JsonProcessingException {
        VehicleLatestSignal vehicleLatestSignal = VehicleLatestSignal.builder()
                .carId(Integer.valueOf(signalWarnEntity.getCarId()))
                .warnLevel(signalWarnEntity.getWarnLevel())
                .batteryType(signalWarnEntity.getBatteryType())
                .warnCode(signalWarnEntity.getWarnId())
                .warnName(signalWarnEntity.getWarnName())
                .signalData(signalWarnEntity.getSignal())
                .build();


        SignalHistory signalHistory = SignalHistory.builder()
                .carId(Integer.valueOf(signalWarnEntity.getCarId()))
                .warnLevel(signalWarnEntity.getWarnLevel())
                .batteryType(signalWarnEntity.getBatteryType())
                .warnCode(signalWarnEntity.getWarnId())
                .warnName(signalWarnEntity.getWarnName())
                .signalData(signalWarnEntity.getSignal())
                .build();

        String carId = signalWarnEntity.getCarId();
        boolean locked = false;
        RLock lock = null;
        try {
            String lockKey = Constants.RedisKey.BATTERY_UPDATE_KEY + carId;
            // 1) 尝试获取分布式锁：等待最多3秒，锁5秒后自动释放
            lock = redisService.getLock(lockKey);
            locked = lock.tryLock(3, 5, TimeUnit.SECONDS);
            if (!locked) {
                // 拿锁失败：可重试、丢弃或者抛出异常
                log.warn("未能获取车辆 {} 的更新锁，放弃本次写入", carId);
                return;
            }

            // 2) 拿到锁以后，先路由，再打开事务
            // 以carId作为切分键，通过 doRouter 设定路由【这样就保证了下面的操作，都是同一个链接下，也就保证了事务的特性】
            dbRouter.doRouter(carId);
            transactionTemplate.execute(status -> {
                try {
                    // 写入全量历史表
                    signalHistoryDao.insert(signalHistory);
                    // 更新最新表
                    int count = vehicleLatestSignalDao.update(vehicleLatestSignal);
                    // 如果没更新到，则插入
                    if (count == 0) {
                        vehicleLatestSignalDao.insert(vehicleLatestSignal);
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    // 若仍出现唯一索引冲突，回滚并抛出
                    status.setRollbackOnly();
                    log.error("写入最新表索引冲突 carId: {} ", carId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), ResponseCode.LOCK_INTERRUPTED.getInfo(),e);
                }
            });

        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new AppException(ResponseCode.LOCK_INTERRUPTED.getCode(), ResponseCode.LOCK_INTERRUPTED.getInfo(),ie);

        } finally {
            String carCache_key=Constants.RedisKey.BATTERY_KEY + carId;
            redisService.remove(carCache_key);
            // 3) 释放路由并解锁（仅当拿到锁时才解锁）
            dbRouter.clear();
            lock.unlock();
        }
    }
    @Override
    public List<VehicleLatestSignalEntity> queryBatterySignal(Integer carId) {
        String cacheKey = Constants.RedisKey.BATTERY_KEY + carId;
        // 1. 尝试从缓存读
        List<VehicleLatestSignal> signals = redisService.getValue(cacheKey);
        if (signals != null) {
            return buildVehicleLatestSignalEntity(signals);
        }
        // 2. 缓存未命中，竞争分布式锁由一个线程去加载
        String lockKey = Constants.RedisKey.BATTERY_LOCK_KEY + carId;
        RLock lock = redisService.getLock(lockKey);
        boolean locked = false;
        try {
            // 最多等待 3 秒去拿锁，拿到锁后 5 秒自动释放
            locked = lock.tryLock(3, 5, TimeUnit.SECONDS);
            if (locked) {
                // 获得锁后，再次 double-check 缓存
                signals = redisService.getValue(cacheKey);
                if (signals != null) {
                    return buildVehicleLatestSignalEntity(signals);
                }
                // 真正去 DB 读
                signals = vehicleLatestSignalDao.selectByCarId(String.valueOf(carId));
                if (signals != null) {
                    // 回填缓存，过期时间按业务场景定（示例：5 秒）
                    redisService.setValue(cacheKey, signals, 50000);
                }
                return buildVehicleLatestSignalEntity(signals);
            } else {
                // 没拿到锁的线程：在短时间内轮询缓存，等第一个线程回填
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < 5000) {
                    signals = redisService.getValue(cacheKey);
                    if (signals != null) {
                        return buildVehicleLatestSignalEntity(signals);
                    }
                    Thread.sleep(50);
                }
                // 超时后仍无缓存，则直接降级查库
                return buildVehicleLatestSignalEntity(vehicleLatestSignalDao.selectByCarId(String.valueOf(carId)));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 被打断也降级查库
            return buildVehicleLatestSignalEntity(vehicleLatestSignalDao.selectByCarId(String.valueOf(carId)));
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public List<VehicleLatestSignalEntity> buildVehicleLatestSignalEntity(List<VehicleLatestSignal> signals){
        if(signals==null) {
            throw new AppException(ResponseCode.NULL_BATTERY_TYPE.getCode(),ResponseCode.NULL_BATTERY_TYPE.getInfo());
        }
        else{
            List<VehicleLatestSignalEntity> vehicleLatestSignalEntities=new ArrayList<>();
            for(VehicleLatestSignal signal:signals){
                VehicleLatestSignalEntity vehicleLatestSignalEntity=VehicleLatestSignalEntity.builder()
                        .id(signal.getId())
                        .carId(signal.getCarId())
                        .warnCode(signal.getWarnCode())
                        .warnName(signal.getWarnName())
                        .warnLevel(signal.getWarnLevel())
                        .batteryType(signal.getBatteryType())
                        .lastUpdated(signal.getLastUpdated())
                        .signalData(signal.getSignalData())
                        .build();
                vehicleLatestSignalEntities.add(vehicleLatestSignalEntity);
            }
            return vehicleLatestSignalEntities;
        }
    }


    public List<VehicleLatestSignalEntity> findAllSignals(){
        List<VehicleLatestSignalEntity> vehicleLatestSignalEntities=new ArrayList<>();
        //遍历所有的库
        for(int dbIdx=1;dbIdx<=dbRouterConfig.getDbCount()-1;dbIdx++){
            DBContextHolder.setDBKey(String.format("%02d", dbIdx));
            log.info("数据库路由 dbIdx：{} ",  dbIdx);
            List<VehicleLatestSignal> vehicleLatestSignals=vehicleLatestSignalDao.findAll();

            if(vehicleLatestSignals!=null){
                vehicleLatestSignalEntities.addAll(buildVehicleLatestSignalEntity(vehicleLatestSignals));
            }
            dbRouter.clear();
        }
        return vehicleLatestSignalEntities;




    }
}
