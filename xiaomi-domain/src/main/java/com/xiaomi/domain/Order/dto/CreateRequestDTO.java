package com.xiaomi.domain.Order.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * ClassName: CreateRequestDTO
 * Package: com.xiaomi.domain.Order.model.dto
 */
@Data
@Builder
public class CreateRequestDTO {
    @NotBlank(message = "用户ID不能为空")
    private String userID;

    private String couponID;

    @Valid
    private AddressDTO address;

    @NotEmpty(message = "订单项不能为空")
    private List<@Valid CreateRequestItemDTO> items;
}
