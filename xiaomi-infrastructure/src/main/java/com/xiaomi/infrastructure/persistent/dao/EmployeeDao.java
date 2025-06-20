package com.xiaomi.infrastructure.persistent.dao;

import com.xiaomi.infrastructure.persistent.po.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
* ClassName: EmployeeDao
* Package: com.xiaomi.infrastructure.persistent.dao
*/
@Mapper
public interface EmployeeDao {
    void insert(Employee employee);
    int update(Employee employee);//返回被影响的行数
    void deleteById(Long id);
    Employee selectById(Long id);
}
