package com.xiaomi.infrastructure.persistent.event;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.xiaomi.infrastructure.persistent.po.HotelRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * ClassName: Service1
 * Package: com.example.cn.infrastructure.event
 */
@Slf4j
@Service
public class FileToMqService {
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    private final ObjectMapper mapper = new ObjectMapper();


    /**
     * 读取指定路径的文件，每行解析成 HotelRecord，并发到指定 topic
     */
    public void sendFile(String filePath, String topic) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            lines.forEach(line -> {
                try {
                    HotelRecord record = mapper.readValue(line, HotelRecord.class);
                    rocketMQTemplate.convertAndSend(topic, line);
                }
                catch (JsonProcessingException e) {
                    // 反序列化失败，记录日志跳过
                    log.error("解析失败: {} » {}", line, e.getMessage());
                }
            });
        }
        catch (IOException e) {
            log.error("无法读取文件 {}: {}", filePath, e.getMessage());
        }
    }
}
