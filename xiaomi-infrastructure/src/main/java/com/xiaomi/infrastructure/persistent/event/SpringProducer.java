package com.xiaomi.infrastructure.persistent.event;



import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * ClassName: SpringProducer
 * Package: com.example.cn.infrastructure.redis
 */
@Component
public class SpringProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(String topic,String msg){
        this.rocketMQTemplate.convertAndSend(topic,msg);
    }
}