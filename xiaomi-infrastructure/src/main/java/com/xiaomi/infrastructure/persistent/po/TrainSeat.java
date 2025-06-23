package com.xiaomi.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ClassName: TrainSeat
 * Package: com.xiaomi.infrastructure.persistent.po
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainSeat {

    private Long id;
    private String trainNo;
    private String carriageNo;
    private String seatNo;
    private Byte status;
    private Integer version;
    private LocalDateTime lockUntil;
}

