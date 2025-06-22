package com.xiaomi.test;


import com.xiaomi.infrastructure.persistent.event.FileToMqService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest                                   // 指定 classes=Application.class 也行，通常可以省略
public class HomeworkTest {

    @Resource
    private FileToMqService fileToMqService;

    @Test
    public void testSendFile() throws Exception {
        String path  = "src/main/resources/output.txt";
        String topic = "hotel-record-topic";
        fileToMqService.sendFile(path, topic);
        // …这里加个 Mockito.verify 或者至少不抛异常就算过
    }
}
