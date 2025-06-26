package com.xiaomi.domain.battery.service.report.rule;

/**
 * ClassName: AbstractWarnChain
 * Package: com.xiaomi.domain.battery.service.report.rule
 */
public abstract class AbstractWarnChain implements IWarnChain {
    private IWarnChain next;

    @Override
    public IWarnChain next() {
        return next;
    }

    @Override
    public IWarnChain appendNext(IWarnChain next) {
        this.next = next;
        return next;
    }

}
