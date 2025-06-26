package com.xiaomi.domain.battery.service.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiaomi.domain.battery.model.entity.SignalEntity;
import com.xiaomi.domain.battery.model.entity.SignalWarnEntity;
import com.xiaomi.domain.battery.repository.IBatteryRepository;
import com.xiaomi.domain.battery.service.armory.RuleArmory;
import com.xiaomi.domain.battery.service.report.rule.IWarnChain;
import com.xiaomi.domain.battery.service.report.rule.factory.DefaultChainFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Stream;

/**
 * ClassName: ReportWarn
 * Package: com.xiaomi.domain.battery.service.report
 */
@Slf4j
@Service
public class ReportWarn implements IReportWarn {
    protected IBatteryRepository batteryRepository;
    protected final DefaultChainFactory defaultChainFactory;
    protected RuleArmory ruleArmory;
    protected ThreadPoolExecutor executor;
    public ReportWarn(IBatteryRepository repository, DefaultChainFactory defaultChainFactory, RuleArmory ruleArmory,ThreadPoolExecutor executor) {
        this.batteryRepository = repository;
        this.defaultChainFactory = defaultChainFactory;
        this.ruleArmory=ruleArmory;
        this.executor = executor;
    }

    @Override
    public List<SignalWarnEntity> performReport(List<SignalEntity> signalEntities){
        List<SignalWarnEntity> result = new ArrayList<>();

        for (SignalEntity signal : signalEntities) {
            // 1. 责任链校验
            raffleLogicChain(signal);
            // 2. 根据warnId计算告警等级
            if (signal.getWarnId() == null) {
                // 情况1：warnId为null时生成两条记录
                List<SignalWarnEntity> signalWarnEntities = handleNullWarnId(signal);
                result.addAll(signalWarnEntities);
                //开启线程发送，提高发送效率。配置的线程池策略为 CallerRunsPolicy
                signalWarnEntities.forEach(entity ->
                        executor.execute(() -> {
                            try {
                                batteryRepository.writeSignal(entity);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        })
                );

            } else {
                // 情况2：warnId为1或2时生成单条记录
                SignalWarnEntity signalWarnEntity = handleSpecificWarnId(signal);
                result.add(signalWarnEntity);
                //开启线程发送，提高发送效率。配置的线程池策略为 CallerRunsPolicy
                executor.execute(() -> {
                    try {
                        batteryRepository.writeSignal(signalWarnEntity);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        return result;
    }
    // 处理warnId为null的情况（返回两个实体）
    private List<SignalWarnEntity> handleNullWarnId(SignalEntity signal) {
        List<SignalWarnEntity> entities = new ArrayList<>();

        // 电压差告警（warnId=1）
        if (signal.getMxMi() != null) {
            Integer mxLevel = ruleArmory.getWarnLevel(
                    signal.getBatteryType(), 1, signal.getMxMi());

            entities.add(buildWarnEntity(signal, 1, mxLevel));
        }

        // 电流差告警（warnId=2）
        if (signal.getLxLi() != null) {
            Integer lxLevel = ruleArmory.getWarnLevel(
                    signal.getBatteryType(), 2, signal.getLxLi());

            entities.add(buildWarnEntity(signal, 2, lxLevel));
        }

        return entities.isEmpty() ?
                Collections.singletonList(buildFailedWarnEntity(signal)) : entities;
    }
    // 处理warnId为1或2的情况（返回单个实体）
    private SignalWarnEntity handleSpecificWarnId(SignalEntity signal) {
        float value = signal.getWarnId() == 1 ?
                signal.getMxMi() : signal.getLxLi();

        Integer warnLevel = ruleArmory.getWarnLevel(
                signal.getBatteryType(), signal.getWarnId(), value);

        return buildWarnEntity(signal, signal.getWarnId(), warnLevel);
    }
    // 责任链校验失败时构建的实体
    private SignalWarnEntity buildFailedWarnEntity(SignalEntity signalEntity) {
        return SignalWarnEntity.builder()
                .carId(signalEntity.getCarId())
                .batteryType(signalEntity.getBatteryType())
                .warnId(signalEntity.getWarnId())
                .signal(signalEntity.getSignal())
                .warnLevel(-1) // 校验失败时设为-1
                .build();
    }

    // 构建成功响应的实体
    private SignalWarnEntity buildWarnEntity(SignalEntity source, Integer warnId, Integer warnLevel) {
        return SignalWarnEntity.builder()
                .carId(source.getCarId())
                .batteryType(source.getBatteryType())
                .warnId(warnId)
                .signal(source.getSignal())
                .warnLevel(warnLevel)
                .build();
    }

    public boolean raffleLogicChain(SignalEntity signalEntity) {
        IWarnChain warnChain = defaultChainFactory.openActionChain();
        return warnChain.action(signalEntity);
    }
}
