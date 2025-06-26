package com.xiaomi.domain.battery.model.entity;

import lombok.*;

/**
 * ClassName: SignalWarnEntity
 * Package: com.xiaomi.domain.battery.model.entity
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignalWarnEntity {
    private String carId;
    private String batteryType;
    private Integer warnId;
    private Integer warnLevel;
    private String signal;
    // 内嵌枚举（移除不必要的注解）

    // 建议添加的辅助方法
    public String getWarnName() {
        return switch (warnId) {
            case 1 -> "电压差报警";
            case 2 -> "电流差报警";
            default -> "未知规则";
        };
    }
}
