package com.xiaomi.infrastructure.persistent.dao;

import com.xiaomi.infrastructure.persistent.po.WarningRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: WarningRulesDao
 * Package: com.xiaomi.infrastructure.persistent.dao
 */
@Mapper
public interface WarningRulesDao {
    List<WarningRule> selectrules();
}