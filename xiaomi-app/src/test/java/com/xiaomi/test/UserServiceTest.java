package com.xiaomi.test;

import com.xiaomi.infrastructure.persistent.event.SpringProducer;
import com.xiaomi.infrastructure.persistent.po.User;
import com.xiaomi.infrastructure.persistent.repository.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    // 注入 UserService
    @Resource
    private UserService userService;


    // 测试 getUserById 方法
    @Test
    public void testGetUserById() {
        Long userId = 2L;

        // 直接调用 service 的方法
        User user = userService.getUserById(userId.toString());
        System.out.println(user);
    }

    // 测试 insertUser 方法
    @Test
    public void testInsertUser() {
        User user = new User("1", "Lijianchen", "lijianchen@cug.edu.cn");
        // 调用 insertUser 方法
        int result = userService.insertUser(user);
        User user2 = new User("2", "Yuan Li", "yuan.li@cug.edu.cn");

        int result2 = userService.insertUser(user2);
        User user3 = new User("3", "Zhang Wei", "zhang.wei@cug.edu.cn");
        int result3 = userService.insertUser(user3);

        User user4 = new User("4", "Wang Fang", "wang.fang@cug.edu.cn");
        int result4 = userService.insertUser(user4);

        User user5 = new User("5", "Liu Yang", "liu.yang@cug.edu.cn");
        int result5 = userService.insertUser(user5);

        User user6 = new User("6", "Chen Hao", "chen.hao@cug.edu.cn");
        int result6 = userService.insertUser(user6);

        User user7 = new User("7", "Zhao Min", "zhao.min@cug.edu.cn");
        int result7 = userService.insertUser(user7);

        User user8 = new User("8", "Qian Zhong", "qian.zhong@cug.edu.cn");
        int result8 = userService.insertUser(user8);

        User user9 = new User("9", "Sun Lei", "sun.lei@cug.edu.cn");
        int result9 = userService.insertUser(user9);

        User user10 = new User("10", "Li Na", "li.na@cug.edu.cn");
        int result10 = userService.insertUser(user10);
    }

    // 测试 getUserByEmail 方法
    @Test
    public void testGetUserByEmail() {
        String email = "jane.doe@example.com";
        List<User> users = userService.getUserByEmail(email);
    }
}
