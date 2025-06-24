package com.xiaomi.domain.Order.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: AddressDTO
 * Package: com.xiaomi.domain.Order.model.dto
 */
@Data
@Builder
public class AddressDTO {
    @NotBlank(message = "收件人姓名不能为空")
    private String contactName;

    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;

    @NotBlank(message = "详情地址不能为空")
    private String detail;

    private String province;
    private String city;
    private String country;
    private String postalCode;
}