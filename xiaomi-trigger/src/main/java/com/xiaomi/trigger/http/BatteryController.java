package com.xiaomi.trigger.http;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomi.domain.battery.model.entity.SignalEntity;
import com.xiaomi.domain.battery.model.entity.SignalWarnEntity;
import com.xiaomi.domain.battery.model.entity.VehicleLatestSignalEntity;
import com.xiaomi.domain.battery.repository.IBatteryRepository;
import com.xiaomi.domain.battery.service.armory.IRuleArmory;
import com.xiaomi.domain.battery.service.report.IReportWarn;
import com.xiaomi.infrastructure.persistent.po.VehicleLatestSignal;
import com.xiaomi.infrastructure.persistent.repository.BatteryRepository;
import com.xiaomi.trigger.api.dto.SignalRequestDTO;
import com.xiaomi.trigger.api.dto.VehicleLatestSignalDTO;
import com.xiaomi.trigger.api.dto.VehicleLatestWarnDTO;
import com.xiaomi.trigger.api.dto.WarnResponseDTO;
import com.xiaomi.types.enums.ResponseCode;
import com.xiaomi.types.exception.AppException;
import com.xiaomi.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ClassName: BatteryController
 * Package: com.xiaomi.trigger.http
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class BatteryController {

    @Resource
    private IRuleArmory ruleArmory;
    @Resource
    private IReportWarn reportWarn;
    @Autowired
    private IBatteryRepository batteryRepository;

    @PostMapping("/warn")
    public Response<List<WarnResponseDTO>> warn(@RequestBody List<SignalRequestDTO> requestList) {
        try {
            log.info("接收到信号上报请求，数量：{}", requestList.size());
            List<SignalEntity> signalEntities=new ArrayList<SignalEntity>();
            for(SignalRequestDTO requestDTO : requestList) {
                SignalEntity signalEntity = SignalEntity.builder()
                                .carId(String.valueOf(requestDTO.getCarId()))
                                .warnId(requestDTO.getWarnId())
                                .signal(requestDTO.getSignal())
                                .build();
                signalEntities.add(signalEntity);
            }
            List<SignalWarnEntity> signalWarnEntities=reportWarn.performReport(signalEntities);
            List<WarnResponseDTO> warnResponseDTOS=new ArrayList<>();
            for(SignalWarnEntity signalWarnEntity : signalWarnEntities) {
                WarnResponseDTO warnResponseDTO = WarnResponseDTO.builder()
                                .carId(Integer.valueOf(signalWarnEntity.getCarId()))
                                .batteryType(signalWarnEntity.getBatteryType())
                                .build();
                warnResponseDTO.setWarnName(signalWarnEntity.getWarnId());
                warnResponseDTO.setWarnLevel(signalWarnEntity.getWarnLevel());
                warnResponseDTOS.add(warnResponseDTO);
            }
            return Response.<List<WarnResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo()) // 按图片要求返回"ok"
                    .data(warnResponseDTOS)
                    .build();
        } catch (AppException e) {
            log.error("信号处理失败", e);
            return Response.<List<WarnResponseDTO>>builder()
                    .code(e.getCode())
                    .info(e.getInfo()) // 按图片要求返回具体错误
                    .build();
        }
    }

    @GetMapping("/query_warn")
    public Response<List<VehicleLatestWarnDTO>> queryWarn(@RequestParam int carId) {
        try {
            log.info("接收到电池预警查询请求：{}", carId);
            List<VehicleLatestSignalEntity> vehicleLatestSignalEntities=batteryRepository.queryBatterySignal(carId);
            List<VehicleLatestWarnDTO> vehicleLatestWarnDTOS=new ArrayList<>();
            for(VehicleLatestSignalEntity vehicleLatestSignalEntity : vehicleLatestSignalEntities) {
                VehicleLatestWarnDTO vehicleLatestWarnDTO = VehicleLatestWarnDTO.builder()
                        .carId(vehicleLatestSignalEntity.getCarId())
                        .batteryType(vehicleLatestSignalEntity.getBatteryType())
                        .warnName(vehicleLatestSignalEntity.getWarnName())
                        .warnCode(vehicleLatestSignalEntity.getWarnCode())
                        .warnLevel(vehicleLatestSignalEntity.getWarnLevel())
                        .build();
                vehicleLatestWarnDTOS.add(vehicleLatestWarnDTO);
            }

            return Response.<List<VehicleLatestWarnDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo()) // 按图片要求返回"ok"
                    .data(vehicleLatestWarnDTOS)
                    .build();
        } catch (AppException e) {
            log.error("预警查询失败", e);
            return Response.<List<VehicleLatestWarnDTO>>builder()
                    .code(e.getCode())
                    .info(e.getInfo()) // 按图片要求返回具体错误
                    .build();
        }
    }

    @GetMapping("/query_signal")
    public Response<VehicleLatestSignalDTO> querySignal(@RequestParam int carId) {
        try {
            log.info("接收到电池信号查询请求：{}", carId);
            List<VehicleLatestSignalEntity> vehicleLatestSignalEntities=batteryRepository.queryBatterySignal(carId);
            VehicleLatestSignalEntity vehicleLatestSignalEntity=vehicleLatestSignalEntities.get(0);
            VehicleLatestSignalDTO vehicleLatestSignalDTO = VehicleLatestSignalDTO.builder()
                    .carId(vehicleLatestSignalEntity.getCarId())
                    .batteryType(vehicleLatestSignalEntity.getBatteryType())
                    .build();
            vehicleLatestSignalDTO.setsignalData(vehicleLatestSignalEntity.getSignalData());
            return Response.<VehicleLatestSignalDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo()) // 按图片要求返回"ok"
                    .data(vehicleLatestSignalDTO)
                    .build();
        } catch (AppException e) {
            log.error("信号查询失败", e);
            return Response.<VehicleLatestSignalDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo()) // 按图片要求返回具体错误
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}