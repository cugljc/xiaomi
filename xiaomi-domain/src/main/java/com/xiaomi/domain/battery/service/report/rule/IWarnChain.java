package com.xiaomi.domain.battery.service.report.rule;

import com.xiaomi.domain.battery.model.entity.SignalEntity;
import com.xiaomi.domain.battery.model.entity.SignalWarnEntity;

/**
 * ClassName: IActionChain
 * Package: com.xiaomi.domain.battery.service.report.rule
 */
public interface IWarnChain extends IWarnnChainArmory,Cloneable {

    boolean action(SignalEntity signalEntity);

}
