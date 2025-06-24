package com.xiaomi.types.common;

/**
 * ClassName: OrderException
 * Package: com.xiaomi.types.common
 */
public class OrderException extends Exception {
    private final String code;

    public OrderException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
