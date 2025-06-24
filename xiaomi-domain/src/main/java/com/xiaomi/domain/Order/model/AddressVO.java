package com.xiaomi.domain.Order.model;

import com.xiaomi.domain.Order.dto.AddressDTO;
import com.xiaomi.types.common.OrderException;
import lombok.Builder;
import lombok.Data;

/**
 * ClassName: AddressVO
 * Package: com.xiaomi.domain.Order.model.dto
 */
@Data
@Builder
public class AddressVO {
    private String contactName;
    private String contactPhone;
    private String detail;
    private String province;
    private String city;
    private String country;
    private String postalCode;

    /**
     * 从 DTO 转换
     */
    public static AddressVO create(AddressDTO dto) throws OrderException {
        AddressVO vo = AddressVO.builder()
                .contactName(dto.getContactName())
                .contactPhone(dto.getContactPhone())
                .detail(dto.getDetail())
                .province(dto.getProvince())
                .city(dto.getCity())
                .country(dto.getCountry())
                .postalCode(dto.getPostalCode())
                .build();
        vo.validate();
        return vo;
    }

    /**
     * 基本校验
     */
    public void validate() throws OrderException {
        if (contactName == null || contactName.isEmpty()) {
            throw new OrderException("BE-ADDR-001", "收件人姓名不能为空");
        }
        if (contactPhone == null || contactPhone.isEmpty()) {
            throw new OrderException("BE-ADDR-002", "联系电话不能为空");
        }
        if (detail == null || detail.isEmpty()) {
            throw new OrderException("BE-ADDR-003", "详情地址不能为空");
        }
    }
}