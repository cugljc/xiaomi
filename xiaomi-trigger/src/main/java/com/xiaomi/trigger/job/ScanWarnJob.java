package com.xiaomi.trigger.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomi.domain.battery.model.entity.VehicleLatestSignalEntity;
import com.xiaomi.domain.battery.repository.IBatteryRepository;
import com.xiaomi.infrastructure.persistent.po.BatteryWarnMessageLog;
import com.xiaomi.infrastructure.persistent.dao.BatteryWarnMessageLogDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;

/**
 * ClassName: ScanWarnJon
 * Package: com.xiaomi.trigger.job
 */
@Slf4j
@Component
public class ScanWarnJob {

    @Resource private IBatteryRepository batteryRepository;
    @Resource private RocketMQTemplate rocketMQTemplate;
    @Resource private BatteryWarnMessageLogDao messageLogDao;

    @Scheduled(cron = "0/20 * * * * ?") // 每20秒扫描一次
    public void exec() {
        List<VehicleLatestSignalEntity> list = batteryRepository.findAllSignals(); // 查全表或分页查
        for (VehicleLatestSignalEntity signalEntity : list) {
            try {
                String payload = new ObjectMapper().writeValueAsString(signalEntity);
                // 1. 发 MQ 消息
                rocketMQTemplate.convertAndSend("battery-warn-topic", payload);
                // 2. 写入补偿表（可异步插入）
                BatteryWarnMessageLog batteryWarnMessageLog = BatteryWarnMessageLog.builder()
                        .carId(signalEntity.getCarId())
                        .payload(payload)
                        .status(0)
                        .build();
                messageLogDao.insert(batteryWarnMessageLog);

                log.info("发送电池预警信号 MQ 成功，carId={}", signalEntity.getCarId());
            } catch (Exception e) {
                log.error("发送电池预警失败，carId={}", signalEntity.getCarId(), e);
            }
        }
    }
}

