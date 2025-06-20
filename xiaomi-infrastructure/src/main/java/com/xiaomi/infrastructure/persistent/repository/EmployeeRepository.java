package com.xiaomi.infrastructure.persistent.repository;

import ch.qos.logback.classic.Logger;
import com.xiaomi.infrastructure.persistent.dao.EmployeeDao;
import com.xiaomi.infrastructure.persistent.po.Employee;
import com.xiaomi.infrastructure.persistent.redis.IRedisService;
import com.xiaomi.types.enums.ResponseCode;
import com.xiaomi.types.exception.AppException;
import org.redisson.api.RLock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import com.xiaomi.types.common.Constants;

/**
 * ClassName: EmployeeResipostory
 * Package: com.xiaomi.infrastructure.persistent.repository
 */
@Slf4j
@Repository
public class EmployeeRepository {
    @Resource
    private IRedisService redisService;
    @Resource
    private EmployeeDao employeeDao;
    @Resource
    private TransactionTemplate transactionTemplate;

    public Employee queryEmployeeId(Long id){
        String cacheKey = Constants.RedisKey.EMPLOYEE_KEY+id.toString();
        Employee employee = redisService.getValue(cacheKey);
        if (null != employee) {
            return employee;
        }
        employee = employeeDao.selectById(id);
        if (employee == null) {
            redisService.setValue(cacheKey, new Employee());//缓存空对象
            return null;
        }
        redisService.setValue(cacheKey, employee);
        return employee;
    }
    // 新增员工
    public boolean insertEmployee(Employee employee) {
        employeeDao.insert(employee);
        return true;
    }
    //这里加锁肯定不会发生数据库缓存不一致
    public int updateEmployee(Employee employee) {
        String cacheKey =  Constants.RedisKey.EMPLOYEE_KEY+employee.getId().toString();
        int result = employeeDao.update(employee);
        if (result > 0) {
            // 更新成功后，删除缓存（而不是更新缓存）
            redisService.remove(cacheKey);
        }
        if(result == 0){
            employeeDao.insert(employee);
        }
        return result;
    }

    // 删除员工（先删除数据库，再删除缓存）
    public boolean deleteEmployeeById(Long id) {
        String cacheKey = Constants.RedisKey.EMPLOYEE_KEY+id.toString();
        employeeDao.deleteById(id);
        redisService.remove(cacheKey);
        return true;
    }
}
