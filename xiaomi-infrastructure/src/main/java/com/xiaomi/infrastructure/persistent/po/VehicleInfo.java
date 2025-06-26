package com.xiaomi.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: VehicleInfo
 * Package: com.xiaomi.infrastructure.persistent.po
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleInfo {
    private Long id;
    private String vid; // 对应CHAR(16)
    private String carId;
    private String batteryType;
    private Integer totalMileageKm;
    private Integer healthPct;
}
