package com.xiaomi.domain.Order.model;

import lombok.Builder;
import lombok.Data;

/**
 * ClassName: Sku
 * Package: com.xiaomi.domain.Order.model
 */
@Data
@Builder
public class Sku {
    /** 商品编号 */
    private String sku;
    /** 商品名称 */
    private String name;
    /** 单位价格（分） */
    private int price;
    /** 商家 ID */
    private String merchantID;
}
