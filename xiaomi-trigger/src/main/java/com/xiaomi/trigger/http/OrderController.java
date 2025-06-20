package com.xiaomi.trigger.http;

import com.xiaomi.trigger.api.IOder;
import com.xiaomi.types.enums.ResponseCode;
import com.xiaomi.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ClassName: RaffleController
 * Package: com.xiaomi.trigger.http
 */
@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/${app.config.api-version}")
public class OrderController implements IOder {
    @Resource
    private ThreadPoolExecutor executor;
    @RequestMapping(value = "getorderid", method = RequestMethod.GET)
    @Override
    public CompletableFuture<Response<String>> generateOrderIdAsync() {
        return CompletableFuture.supplyAsync(() -> {
            log.info("OrderService: 开始生成订单号，当前线程：{}", Thread.currentThread().getName());
            try {
                String orderId = RandomStringUtils.randomNumeric(11);
                Response<String> resp = Response.<String>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(orderId)
                        .build();
                log.info("OrderService: 订单号生成完成：{}", orderId);
                return resp;
            } catch (Exception ex) {
                log.error("OrderService: 生成订单号失败", ex);
                return Response.<String>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info(ResponseCode.UN_ERROR.getInfo())
                        .build();
            }
        }, executor);
    }


}
