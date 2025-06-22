package com.xiaomi.trigger.http;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.xiaomi.infrastructure.persistent.po.HotelRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: HotelCtroller
 * Package: com.xiaomi.trigger.http
 */
@RestController
@RequestMapping("/api/hotels")
public class HotelSearchController {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @GetMapping("/search")
    public ResponseEntity<?> searchHotels(
            @RequestParam String managerName) throws IOException {

        SearchResponse<HotelRecord> response = elasticsearchClient.search(s -> s
                        .index("hotels_ljc")
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m.term(t -> t.field("manager_name.keyword").value(managerName)))
                                        .must(f -> f.bool(bb -> bb
                                                .should(sh -> sh.wildcard(w -> w.field("hotel_name.keyword").value("*如家*")))
                                                .should(sh -> sh.bool(b2 -> b2
                                                        .must(mm -> mm.range(r -> r.number(nqr->nqr.field("score").gt(3.0))))
                                                        .must(mm -> mm.range(r -> r.number(nqr->nqr.field("price").lt(500.0))))
                                                ))
                                        ))
                                )
                        )
                , HotelRecord.class);

        List<HotelRecord> hotels = response.hits().hits().stream()
                .map(hit -> hit.source())
                .collect(Collectors.toList());

        return ResponseEntity.ok(hotels);
    }
}

