package com.xiaomi.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName: ProjectDto
 * Package: com.xiaomi.trigger.api.dto
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {
    private Long id;         // 商品ID
    private String name;     // 商品名称
    private Integer stock;
}
