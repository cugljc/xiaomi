package com.xiaomi.infrastructure.persistent.repository;

/**
 * ClassName: UserService
 * Package: com.xiaomi.infrastructure.persistent.repository
 */
import com.xiaomi.infrastructure.persistent.dao.UserDao;
import com.xiaomi.infrastructure.persistent.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;  // 依赖注入 DAO 接口

    /**
     * 根据 userId 获取用户信息（动态表选择）
     *
     * @param id 用户 ID
     * @return 用户信息
     */
    public User getUserById(String id) {
        // 查询用户信息
        return userDao.queryUserInfoByUserId(id);
    }

    /**
     * 插入用户信息（动态表选择）
     *
     * @param user 用户对象
     * @return 插入操作的行数
     */
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }

    /**
     * 基于 email 模糊查询用户信息（动态表选择）
     *
     * @param email 用户邮箱
     * @return 用户列表
     */
    public List<User> getUserByEmail(String email) {

        return userDao.queryUserByEmail(email);
    }


}
