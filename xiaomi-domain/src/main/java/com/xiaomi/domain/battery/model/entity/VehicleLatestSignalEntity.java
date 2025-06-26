package com.xiaomi.domain.battery.model.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * ClassName: VehicleLatestSignalEntity
 * Package: com.xiaomi.domain.battery.model.entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleLatestSignalEntity {
    private Long id;
    private Integer carId;
    private Integer warnCode;
    private String warnName;
    private Integer warnLevel;
    private String batteryType;
    private String signalData;
    private Date lastUpdated;

}
