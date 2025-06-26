package com.xiaomi.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: WarnResponseDTO
 * Package: com.xiaomi.trigger.api.dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarnResponseDTO {
    private Integer carId;
    private String batteryType;
    private String warnName;
    private String warnLevel;

    // 新增方法：根据规则ID获取名称
    public void setWarnName(Integer warnId) {
        this.warnName= switch (warnId) {
            case 1 -> "电压差报警";
            case 2 -> "电流差报警";
            default -> "未知规则";
        };
    }
    public void setWarnLevel(Integer warnLevel) {
        if(warnLevel==null)this.warnLevel="不报警";
        else if(warnLevel==-1)this.warnLevel="输入参数错误";
        else this.warnLevel=String.valueOf(warnLevel);
    }
}