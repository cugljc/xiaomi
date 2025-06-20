package com.xiaomi.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Fuzhengwei bugstack.cn @李健宸
 * @description 抽奖奖品实体
 * @create 2024-01-06 09:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Long id;
    private String name;
    private Integer gender;
    private String phone;
    private String email;
    private Date birthDate;
    private Date hireDate;
    private Long managerId;
    private Long position;
    private Integer status;
    private Date gmtCreated;
    private Date gmtModify;
}
