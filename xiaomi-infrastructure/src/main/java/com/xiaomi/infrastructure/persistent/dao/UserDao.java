package com.xiaomi.infrastructure.persistent.dao;

import com.xiaomi.infrastructure.persistent.dbrouter.DBRouter;
import com.xiaomi.infrastructure.persistent.dbrouter.DBRouterStrategy;
import com.xiaomi.infrastructure.persistent.po.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: UserDao
 * Package: com.xiaomi.infrastructure.persistent.dao
 */
@DBRouterStrategy(splitTable = true)
@Mapper
public interface UserDao {

    @DBRouter(key = "id")
    User queryUserInfoByUserId(String id);

    @DBRouter(key = "id")
    int insertUser(User req);

    @DBRouter(key = "email")
    List<User> queryUserByEmail(String email);

}