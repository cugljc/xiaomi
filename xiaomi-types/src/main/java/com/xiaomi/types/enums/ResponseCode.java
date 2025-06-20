package com.xiaomi.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    EMPLOYEE_INSERT_ERROR("ERR_BIZ_010","员工表插入失败")
    ;

    private String code;
    private String info;

}
