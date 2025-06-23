package com.xiaomi.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: User
 * Package: com.xiaomi.infrastructure.persistent.po
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;           // 主键ID
    private String name;       // 用户名称
    private String email;
}