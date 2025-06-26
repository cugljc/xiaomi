package com.xiaomi.test;

/**
 * ClassName: BatteryTest
 * Package: com.xiaomi.test
 */
import com.xiaomi.domain.battery.model.entity.VehicleInfoEntity;
import com.xiaomi.domain.battery.repository.IBatteryRepository;
import com.xiaomi.domain.battery.service.armory.RuleArmory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BatteryTest {
    @Resource
    private RuleArmory ruleArmory;
    @Resource
    private IBatteryRepository batteryRepository;
    @Test
    public void testArmory() throws Exception {
        ruleArmory.assembleRules();
        log.info("警告类型{}",ruleArmory.getWarnLevel("三元电池",1,1.01f));
    }

    @Test
    public void testVeichle() throws Exception {
        // 模拟图片中的三行数据
        List<VehicleInfoEntity> demoData = Arrays.asList(
                // 图片中的原始三条数据（保持不变）
                VehicleInfoEntity.builder()
                        .carId("1")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("三元电池")
                        .totalMileageKm(100)
                        .healthPct(100)
                        .build(),
                VehicleInfoEntity.builder()
                        .carId("2")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("铁锂电池")
                        .totalMileageKm(600)
                        .healthPct(95)
                        .build(),
                VehicleInfoEntity.builder()
                        .carId("3")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("三元电池")
                        .totalMileageKm(300)
                        .healthPct(98)
                        .build(),

                // 新增的10条数据（carId 4-13）
                VehicleInfoEntity.builder()
                        .carId("4")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("三元电池")
                        .totalMileageKm(350)
                        .healthPct(97)
                        .build(),
                VehicleInfoEntity.builder()
                        .carId("5")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("三元电池")
                        .totalMileageKm(420)
                        .healthPct(96)
                        .build(),
                VehicleInfoEntity.builder()
                        .carId("6")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("三元电池")
                        .totalMileageKm(280)
                        .healthPct(99)
                        .build(),
                VehicleInfoEntity.builder()
                        .carId("7")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("铁锂电池")
                        .totalMileageKm(550)
                        .healthPct(94)
                        .build(),
                VehicleInfoEntity.builder()
                        .carId("8")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("三元电池")
                        .totalMileageKm(320)
                        .healthPct(97)
                        .build(),
                VehicleInfoEntity.builder()
                        .carId("9")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("铁锂电池")
                        .totalMileageKm(480)
                        .healthPct(93)
                        .build(),
                VehicleInfoEntity.builder()
                        .carId("10")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("三元电池")
                        .totalMileageKm(380)
                        .healthPct(96)
                        .build(),
                VehicleInfoEntity.builder()
                        .carId("11")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("三元电池")
                        .totalMileageKm(290)
                        .healthPct(98)
                        .build(),
                VehicleInfoEntity.builder()
                        .carId("12")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("铁锂电池")
                        .totalMileageKm(510)
                        .healthPct(92)
                        .build(),
                VehicleInfoEntity.builder()
                        .carId("13")
                        .vid("VIN" + RandomStringUtils.randomNumeric(13))
                        .batteryType("三元电池")
                        .totalMileageKm(360)
                        .healthPct(95)
                        .build()
        );

        // 批量插入
        demoData.stream()
                .map(batteryRepository::insertVehicleInfo)
                .reduce(0, Integer::sum);
        log.info(batteryRepository.selectBatteryType("4"));
    }
}
