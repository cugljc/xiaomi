package com.xiaomi.domain.Order.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * ClassName: CreateRequestItemDTO
 * Package: com.xiaomi.domain.Order.model.dto
 */
@Data
@Builder
public class CreateRequestItemDTO {
    @NotBlank(message = "sku 不能为空")
    private String sku;

    @Min(value = 1, message = "数量必须大于 0")
    private int quantity;
}