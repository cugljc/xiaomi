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
 * ClassName: VehicleLatestSignaDTO
 * Package: com.xiaomi.trigger.api.dto
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleLatestSignalDTO {
    private Integer carId;
    private String batteryType;
    private Map<String, Object> signalData;
    // 如果仍然需要兼容 String 输入，可以添加一个转换方法
    public void setsignalData(String signalJson) throws JsonProcessingException {
        if (StringUtils.isNotBlank(signalJson)) {
            ObjectMapper mapper = new ObjectMapper();
            this.signalData = mapper.readValue(signalJson, new TypeReference<Map<String, Object>>() {});
        }
    }

}
