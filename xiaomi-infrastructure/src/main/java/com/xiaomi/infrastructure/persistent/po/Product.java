package com.xiaomi.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: Product
 * Package: com.xiaomi.infrastructure.persistent.po
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;         // 商品ID
    private String name;     // 商品名称
    private Integer stock;
}
