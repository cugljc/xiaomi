package com.xiaomi.infrastructure.persistent.po;

/**
 * ClassName: VehicleLatestSignal
 * Package: com.xiaomi.infrastructure.persistent.po
 */



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleLatestSignal {
    private Long id;
    private Integer carId;
    private Integer warnCode;
    private String warnName;
    private Integer warnLevel;
    private String batteryType;
    private String signalData;
    private Date lastUpdated;


}
