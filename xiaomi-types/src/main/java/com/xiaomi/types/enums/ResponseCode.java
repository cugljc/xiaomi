package com.xiaomi.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "ok"),
    UN_ERROR("0001", "false"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    INDEX_DUP("0003", "唯一索引冲突"),
    EMPLOYEE_INSERT_ERROR("ERR_BIZ_010","员工表插入失败"),
    NULL_BATTERY_TYPE("1001", "车架号不存在"),
    INVALID_WARN_ID("1002", "告警ID不存在"),
    INVALID_JSON_FORMAT("1003", "JSON格式非法"),
    LOCK_INTERRUPTED("0004","锁中断")
    ;

    private String code;
    private String info;

}
