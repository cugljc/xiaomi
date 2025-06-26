package com.xiaomi.trigger.api.dto;

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
 * ClassName: VehicleLatestWarnDTO
 * Package: com.xiaomi.trigger.api.dto
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleLatestWarnDTO {
    private Integer carId;
    private String batteryType;
    private String warnName;
    private Integer warnCode;
    private Integer warnLevel;

}
