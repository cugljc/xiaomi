package com.xiaomi.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * ClassName: BatteryWarnMessageLog
 * Package: com.xiaomi.infrastructure.persistent.dao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryWarnMessageLog {
    private Long id;
    private Integer carId;
    private String payload;
    private Integer status; // 0=未处理, 1=成功
    private Integer retryCount;
    private Date lastRetryTime;
    private Date createTime;
}
