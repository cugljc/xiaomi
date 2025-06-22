package com.xiaomi.trigger.listener;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.xiaomi.infrastructure.persistent.po.HotelRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * ClassName: HotelIndexService
 * Package: com.example.cn.trigger.listener
 */
@Slf4j
@Service
public class HotelIndexService {

    private final ElasticsearchClient esClient;

    public HotelIndexService(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    public void index(HotelRecord record) throws IOException {
        esClient.index(i -> i
                .index("hotels_ljc")
                .id(record.getId().toString())
                .document(record)    // 直接把 record 传进去
        );

    }
}
