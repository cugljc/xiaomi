package com.xiaomi.domain.battery.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: BatteryEntity
 * Package: com.xiaomi.domain.battery.model.entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarnRuleEntity {
    private Integer id;
    private Integer warnId;  // 改用包装类型
    private String batteryType;
    private Integer warnLevel;  // 改用包装类型
    private Float minVal;      // 改用包装类型处理NULL
    private Float maxVal;      // 改用包装类型处理NULL
    private String warnName;
}
