package com.xiaomi.domain.battery.service.report.rule.impl;

import com.xiaomi.domain.battery.model.entity.SignalEntity;
import com.xiaomi.domain.battery.model.entity.SignalWarnEntity;
import com.xiaomi.domain.battery.repository.IBatteryRepository;
import com.xiaomi.domain.battery.service.report.rule.AbstractWarnChain;
import com.xiaomi.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.xiaomi.types.enums.ResponseCode;

import javax.annotation.Resource;

/**
 * ClassName: WarnCarActionChain
 * Package: com.xiaomi.domain.battery.service.report.rule.impl
 */
@Slf4j
@Component("warn_car_action_chain")
public class WarnCarActionChain extends AbstractWarnChain {
    @Resource
    IBatteryRepository batteryRepository;
    @Override
    public boolean action(SignalEntity signalEntity) {
        log.info("责任链-信号校验开始。carid:{} warnid:{}", signalEntity.getCarId(), signalEntity.getWarnId());
        String batteryType=batteryRepository.selectBatteryType(signalEntity.getCarId());
        if (batteryType==null){
            throw new AppException(ResponseCode.NULL_BATTERY_TYPE.getCode(), ResponseCode.NULL_BATTERY_TYPE.getInfo());
        }
        signalEntity.setBatteryType(batteryType);
        return next().action(signalEntity);

    }
}
