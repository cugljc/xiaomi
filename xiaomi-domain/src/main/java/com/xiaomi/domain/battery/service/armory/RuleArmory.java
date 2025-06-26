package com.xiaomi.domain.battery.service.armory;

import com.xiaomi.domain.battery.model.entity.WarnRuleEntity;
import com.xiaomi.domain.battery.repository.IBatteryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: RuleArmory
 * Package: com.xiaomi.domain.battery.service.armory
 */
@Slf4j
@Service
public class RuleArmory implements IRuleArmory {
    @Resource
    IBatteryRepository batteryRepository;

    @Override
    public void assembleRules(){

        List<WarnRuleEntity> warnRuleEntities=batteryRepository.queryWarnRuleList();
        // 1. 按电池类型和告警代码分组
        Map<String, List<WarnRuleEntity>> groupedRules = warnRuleEntities.stream()
                .collect(Collectors.groupingBy(
                        rule -> String.format("%s_%d",
                                rule.getBatteryType(),
                                rule.getWarnId())
                ));

        int precision=1;
        float epsilon = 0.01f;
        // 2. 处理每组规则
        groupedRules.forEach((groupKey, ruleList) -> {
            // 初始化存储结构
            Map<String, String> redisHash = new HashMap<>();
            float groupMin = Float.MAX_VALUE;
            float groupMax = Float.MIN_VALUE;
            int maxWarnLevel = Integer.MIN_VALUE;
            // 3. 处理每条规则
            for (WarnRuleEntity rule : ruleList) {
                // 更新极值
                groupMin = Math.min(groupMin, rule.getMinVal());
                groupMax = Math.max(groupMax, rule.getMinVal());
                maxWarnLevel = Math.max(maxWarnLevel, rule.getWarnLevel());
                // 离散化区间
                if (rule.getMaxVal() == null) {
                    // 无限区间特殊处理
                    redisHash.put(String.valueOf(rule.getMinVal()),
                            String.valueOf(rule.getWarnLevel()));
                } else {
                    // 常规区间离散化
                    float step = (float) Math.pow(10, -precision);
                    for (float v = rule.getMinVal(); v < rule.getMaxVal()-epsilon; v += step) {
                        String key = String.format("%.1f", v); // 保留1位小数
                        redisHash.put(key, String.valueOf(rule.getWarnLevel()));
                    }
                }
            }
            batteryRepository.storeSearchRateTable(groupKey, redisHash);
            batteryRepository.setSearchMax(groupKey,groupMax);
            batteryRepository.setSearchMaxWarnLevel(groupKey,maxWarnLevel);
        });
    }

    @Override
    public Integer getWarnLevel(String batteryType, int warnCode, float value) {
        String key = String.format("%s_%d", batteryType, warnCode);
        // 格式化查询值
        BigDecimal bd = new BigDecimal(String.valueOf(value));
        String query=String.format("%.1f", bd.setScale(1, RoundingMode.DOWN).floatValue());
        Integer warnlevel=batteryRepository.getWarnLevel(key,query);
        if(warnlevel!=null) return warnlevel;
        if(value>batteryRepository.getMaxValue(key))return 0;
        return null;
    }

}
