package com.xiaomi.domain.battery.service.report.rule.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomi.domain.battery.model.entity.SignalEntity;
import com.xiaomi.domain.battery.service.report.rule.AbstractWarnChain;
import com.xiaomi.types.enums.ResponseCode;
import com.xiaomi.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * ClassName: WarnSignalActionChain
 * Package: com.xiaomi.domain.battery.service.report.rule.impl
 */
@Slf4j
@Component("warn_signal_action_chain")
public class WarnSignalActionChain extends AbstractWarnChain {
    @Override
    public boolean action(SignalEntity signalEntity) {
        try {
            // 1. 基础校验
            if (signalEntity == null || StringUtils.isBlank(signalEntity.getSignal())) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),
                        "信号实体或数据为空");
            }

            // 2. 解析JSON
            JsonNode signalData = new ObjectMapper().readTree(signalEntity.getSignal());

            // 3. 计算原始差值（保留正负）
            if (signalData.has("Mx") && signalData.has("Mi")) {
                float mx = signalData.get("Mx").floatValue();
                float mi = signalData.get("Mi").floatValue();
                signalEntity.setMxMi(mx - mi); // 直接计算差值
            }

            if (signalData.has("Ix") && signalData.has("Ii")) {
                float ix = signalData.get("Ix").floatValue();
                float ii = signalData.get("Ii").floatValue();
                signalEntity.setLxLi(ix - ii); // 直接计算差值
            }

            // 4. 有效性验证
            if (signalEntity.getMxMi() == null && signalEntity.getLxLi() == null) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),
                        "信号必须包含Mx/Mi或Ix/Ii");
            }

            log.info("信号差值计算完成 - MxMi: {}, LxLi: {}",
                    signalEntity.getMxMi(), signalEntity.getLxLi());
            return true;

        } catch (JsonProcessingException e) {
            throw new AppException(ResponseCode.INVALID_JSON_FORMAT.getCode(),
                    "JSON解析失败: " + e.getOriginalMessage());
        } catch (Exception e) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(),
                    "信号处理异常: " + e.getMessage());
        }
    }


}
