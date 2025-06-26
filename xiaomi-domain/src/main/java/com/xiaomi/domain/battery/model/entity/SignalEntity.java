package com.xiaomi.domain.battery.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ClassName: SignalEntity
* Package: com.xiaomi.domain.battery.model.entity 
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignalEntity {
    private String carId;
    private Integer warnId;
    private String signal;
    private String batteryType;
    private Float MxMi;
    private Float LxLi;
}