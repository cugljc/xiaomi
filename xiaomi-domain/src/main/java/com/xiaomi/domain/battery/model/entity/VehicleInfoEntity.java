package com.xiaomi.domain.battery.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: VehicleInfoEntity
 * Package: com.xiaomi.domain.battery.model.entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleInfoEntity {
    private Long id;
    private String vid; // 对应CHAR(16)
    private String carId;
    private String batteryType;
    private Integer totalMileageKm;
    private Integer healthPct;
}