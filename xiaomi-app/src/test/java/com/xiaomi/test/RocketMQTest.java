package com.xiaomi.test;

import com.xiaomi.infrastructure.persistent.event.SpringProducer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * ClassName: RocketMqTest
 * Package: com.xiaomi.test
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RocketMQTest {

    @Resource
    private SpringProducer springProducer;

    @Test
    public void test() throws InterruptedException {
        while (true) {
            springProducer.sendMessage("TestTopic", "我是测试消息");
            Thread.sleep(3000);
        }
    }

}
