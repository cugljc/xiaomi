package com.xiaomi.trigger.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: SignalRequestDTO
 * Package: com.xiaomi.trigger.api.dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignalRequestDTO {
    @JsonProperty("carId")
    private Integer carId;

    @JsonProperty("warnId")
    private Integer warnId;

    private String signal;
}
