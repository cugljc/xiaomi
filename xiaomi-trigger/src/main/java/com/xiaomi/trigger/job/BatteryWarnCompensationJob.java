package com.xiaomi.trigger.job;

import com.xiaomi.domain.task.model.BatteryWarnMessageLogEntity;
import com.xiaomi.infrastructure.persistent.dao.BatteryWarnMessageLogDao;
import com.xiaomi.infrastructure.persistent.po.BatteryWarnMessageLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: BatteryWarnCompensationJob
 * Package: com.xiaomi.trigger.listener
 */
@Slf4j
@Component
public class BatteryWarnCompensationJob {

    @Resource private RocketMQTemplate rocketMQTemplate;
    @Resource
    private BatteryWarnMessageLogDao messageLogDao;

    @Scheduled(cron = "0/3 * * * * ?") // 每5秒重试一次
    public void compensate() {
        List<BatteryWarnMessageLog> logs = messageLogDao.findPendingLogs(10);
        for (BatteryWarnMessageLog logItems : logs) {
            try {
                rocketMQTemplate.convertAndSend("battery-warn-topic", logItems.getPayload());
                // 更新重试信息
                messageLogDao.increaseRetryCount(logItems.getId());
                log.info("补偿重发 MQ 成功，carId={}", logItems.getCarId());
            } catch (Exception e) {
                log.error("补偿发送失败，carId={}, msgId={}", logItems.getCarId(), logItems.getId(), e);
            }
        }
    }
}

