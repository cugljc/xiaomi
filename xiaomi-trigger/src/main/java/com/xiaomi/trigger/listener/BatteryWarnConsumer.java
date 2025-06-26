package com.xiaomi.trigger.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomi.domain.battery.model.entity.VehicleLatestSignalEntity;
import com.xiaomi.infrastructure.persistent.dao.BatteryWarnMessageLogDao;
import com.xiaomi.infrastructure.persistent.po.VehicleLatestSignal;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: BatteryWarnConsumer
 * Package: com.xiaomi.trigger.listener
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "battery-warn-topic",
        consumerGroup = "xiaomi-group",
        messageModel = MessageModel.CLUSTERING // 集群消费
)
public class BatteryWarnConsumer implements RocketMQListener<String> {

    @Autowired
    private BatteryWarnMessageLogDao messageLogDao;

    @Override
    public void onMessage(String message) {
        try {
            VehicleLatestSignalEntity signalEntity = new ObjectMapper().readValue(message, VehicleLatestSignalEntity.class);
            // 更新补偿表为成功
            messageLogDao.markSuccessByCarId(String.valueOf(signalEntity.getCarId()));

            log.info("车辆={}，电池类型={}，警告名称={}，警告级别={}", signalEntity.getCarId(),signalEntity.getBatteryType(),signalEntity.getWarnName(),signalEntity.getWarnLevel());

        } catch (Exception e) {
            log.error("电池预警失败，消息={}，异常={}", message, e.getMessage(), e);
        }
    }
}

