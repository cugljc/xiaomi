package com.xiaomi.domain.battery.service.report;

import com.xiaomi.domain.battery.model.entity.SignalEntity;
import com.xiaomi.domain.battery.model.entity.SignalWarnEntity;

import java.util.List;

/**
 * ClassName: IReport
 * Package: com.xiaomi.domain.battery.service.report
 */
public interface IReportWarn {
    List<SignalWarnEntity> performReport(List<SignalEntity> signalEntities);
}
