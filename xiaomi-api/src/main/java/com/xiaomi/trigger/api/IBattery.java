package com.xiaomi.trigger.api;

import com.xiaomi.types.model.Response;

import java.util.concurrent.CompletableFuture;

/**
 * ClassName: IOder
 * Package: com.xiaomi.api
 */
public interface IBattery {
    public CompletableFuture<Response<String>> generateOrderIdAsync();
}
