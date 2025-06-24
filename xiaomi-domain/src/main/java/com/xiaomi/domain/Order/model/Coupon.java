package com.xiaomi.domain.Order.model;

import com.xiaomi.types.common.OrderException;
import lombok.Builder;
import lombok.Data;

/**
 * ClassName: coupon
 * Package: com.xiaomi.domain.Order.model
 */
@Data
@Builder
public class Coupon {
    public static final String FIXED = "FIXED_AMOUNT";
    public static final String PERCENT = "PERCENTAGE";

    private String id;
    private String userID;
    private String name;
    /** 优惠类型 */
    private String type;
    /** 满 x 可用 */
    private int x;
    /** 减 y 或 折 y （10 表示 10 折） */
    private int y;

    /** 校验可用性 */
    public void canUse(int total) throws OrderException {
        if (total < x) {
            throw new OrderException("BE-COUPON-001", "订单金额未达到优惠券使用门槛");
        }
    }

    /** 计算优惠金额 */
    public int getDiscountAmount(int total) {
        if (FIXED.equals(type)) {
            return y;
        } else if (PERCENT.equals(type)) {
            return total * (10 - y) / 100;
        }
        return 0;
    }
}
