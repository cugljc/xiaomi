package com.xiaomi.trigger.http;

import com.alibaba.fastjson.JSON;
import com.xiaomi.infrastructure.persistent.po.Employee;
import com.xiaomi.infrastructure.persistent.repository.EmployeeRepository;
import com.xiaomi.types.enums.ResponseCode;
import com.xiaomi.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: RaffleController
 * Package: com.xiaomi.trigger.http
 */
@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/${app.config.api-version}/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("select")
    public Response<Employee> getById(@RequestParam Long id) {
        try {
            log.info("查询id开始 id：{}", id);
            Employee emp = employeeRepository.queryEmployeeId(id);
            Response<Employee> response = Response.<Employee>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(emp)
                    .build();
            log.info("查询id完成 id：{} response: {}", id, JSON.toJSONString(response));
            return response;
        }
        catch (Exception e) {
            log.error("查询id失败 id：{}", id, e);
            return Response.<Employee>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Response<Boolean> insertEmployee(@RequestBody Employee employee) {
        try {
            log.info("插入员工开始 → employee={}", JSON.toJSONString(employee));
            Boolean status=employeeRepository.insertEmployee(employee);
            log.info("插入员工完成 → employeeId={}", employee.getId());
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(status)
                    .build();
        } catch (Exception e) {
            log.error("插入员工失败（系统异常） → employee={}",
                    JSON.toJSONString(employee), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }



}
