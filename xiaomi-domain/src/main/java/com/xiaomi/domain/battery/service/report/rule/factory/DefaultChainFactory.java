package com.xiaomi.domain.battery.service.report.rule.factory;

import com.xiaomi.domain.battery.service.report.rule.AbstractWarnChain;
import com.xiaomi.domain.battery.service.report.rule.IWarnChain;
import lombok.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: DefaultChainFactory
 * Package: com.xiaomi.domain.battery.service.report.rule.factory
 */
@Service
public class DefaultChainFactory {
    // 原型模式获取对象
    private final ApplicationContext applicationContext;

    public DefaultChainFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    public IWarnChain openActionChain() {
        // 1. 按顺序创建责任链节点，这里写死了
        // 2.其实可以在库表里存一条责任链的规则，存到redis，取出解析，再进行装配
        IWarnChain head = applicationContext.getBean(
                LogicModel.RULE_CAR.getCode(), IWarnChain.class);

        IWarnChain codeNode = applicationContext.getBean(
                LogicModel.RULE_CODE.getCode(), IWarnChain.class);

        IWarnChain signalNode = applicationContext.getBean(
                LogicModel.RULE_SIGNAL.getCode(), IWarnChain.class);

        // 2. 装配责任链
        head.appendNext(codeNode);
        codeNode.appendNext(signalNode);

        // 3. 返回链头节点
        return head;
    }


    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_CAR("warn_car_action_chain", "车辆校验"),
        RULE_CODE("warn_code_action_chain", "警告代码检验"),
        RULE_SIGNAL("warn_signal_action_chain", "json校验"),
        ;

        private final String code;
        private final String info;

    }

}

