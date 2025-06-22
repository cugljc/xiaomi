package com.xiaomi.trigger.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomi.infrastructure.persistent.po.HotelRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ClassName: listener
 * Package: com.example.cn.trigger.listener
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "xiaomi-group", topic = "hotel-record-topic",consumeMode= ConsumeMode.CONCURRENTLY,messageModel= MessageModel.BROADCASTING)
public class HotelRecordConsumer implements RocketMQListener<String> {

    private final ObjectMapper mapper;
    private final HotelIndexService indexService;

    public HotelRecordConsumer(HotelIndexService indexService) {
        this.indexService = indexService;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void onMessage(String message) {
        try {
            HotelRecord record = mapper.readValue(message, HotelRecord.class);
            indexService.index(record);
            log.info("消费消息解析成功{}", record.getId());
        }
        catch (IOException e) {
            log.error("消费消息解析失败: {}", e.getMessage());
        }
    }
}

