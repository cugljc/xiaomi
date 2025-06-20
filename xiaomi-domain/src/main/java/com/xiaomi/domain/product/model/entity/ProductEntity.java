package com.xiaomi.domain.product.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: ProductEntity
 * Package: com.xiaomi.domain.product.model.entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {
    private Long id;         // 商品ID
    private String name;     // 商品名称
    private Integer stock;
}
