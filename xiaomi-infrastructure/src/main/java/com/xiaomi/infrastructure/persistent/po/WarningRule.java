package com.xiaomi.infrastructure.persistent.po;

/**
 * ClassName: WarningRule
 * Package: com.xiaomi.infrastructure.persistent.po
 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarningRule {
    private Integer id;
    private Integer warnId;  // 改用包装类型
    private String batteryType;
    private Integer warnLevel;  // 改用包装类型
    private Float minVal;      // 改用包装类型处理NULL
    private Float maxVal;      // 改用包装类型处理NULL
    private String warnName;
}
