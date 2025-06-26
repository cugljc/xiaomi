package com.xiaomi.domain.battery.service.report.rule;

/**
 * ClassName: IActionChainArmory
 * Package: com.xiaomi.domain.battery.service.report.rule
 */
public interface IWarnnChainArmory {

    IWarnChain next();

    IWarnChain appendNext(IWarnChain next);

}