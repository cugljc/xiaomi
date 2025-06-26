package com.xiaomi.domain.battery.service.report.rule.impl;

import com.xiaomi.domain.battery.model.entity.SignalEntity;
import com.xiaomi.domain.battery.repository.IBatteryRepository;
import com.xiaomi.domain.battery.service.report.rule.AbstractWarnChain;
import com.xiaomi.types.enums.ResponseCode;
import com.xiaomi.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ClassName: WarnCodeActionChain
 * Package: com.xiaomi.domain.battery.service.report.rule.impl
 */
@Slf4j
@Component("warn_code_action_chain")
public class WarnCodeActionChain extends AbstractWarnChain {
    @Override
    public boolean action(SignalEntity signalEntity) {
        Integer warnId = signalEntity.getWarnId();
        if (warnId!=null&&warnId!=1&&warnId!=2){
            throw new AppException(ResponseCode.INVALID_WARN_ID.getCode(), ResponseCode.INVALID_WARN_ID.getInfo());
        }
        return next().action(signalEntity);

    }
}
