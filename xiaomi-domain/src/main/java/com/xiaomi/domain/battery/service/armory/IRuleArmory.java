package com.xiaomi.domain.battery.service.armory;

/**
 * ClassName: IRuleArmory
 * Package: com.xiaomi.domain.battery.service.armory
 */
public interface IRuleArmory {
    void assembleRules();

    Integer getWarnLevel(String batteryType, int warnCode, float value);
}
